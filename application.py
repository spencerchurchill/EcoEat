from flask import Flask, jsonify, request
from clarifai.rest import ClarifaiApp
from eco import eco


def get_ingredients(url:str):
    clarifai_app = ClarifaiApp(api_key='528ab75695b14ffe8be3e86f918f455a')
    model = clarifai_app.models.get('food-items-v1.0')
    response = model.predict_by_url(url)
    status = response['status']['description']
    items = []

    for food in response['outputs'][0]['data']['concepts']:
        items.append(food['name'])

    if items:
        return status, items, eco.food_quality(items)
    else:
        return items, status, []


app = Flask(__name__)
@app.route('/')
def get_home():
    return "Welcome to EcoEat's Server!"

@app.route('/image', methods=['GET'])
def get_image():
    image_url = request.args.get('url')
    status, items, info = get_ingredients(image_url)
    return jsonify({'status': status, 'data': {'ingredients': items, 'information': info}})


@app.route('/stats', methods=['GET'])
def get_stats():
    lst = request.args.get('list').split('-')
    co2s = [float(i) for i in lst]
    return jsonify({'prediction': eco.tree_predict(co2s)})

@app.route('/graph', methods=['GET'])
def graph():
    co2 = request.args.get('co2').split('-')
    water = request.args.get('water').split('-')
    return eco.graph(co2, water)

if __name__ == '__main__':
    app.run()