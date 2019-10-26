from flask import Flask, jsonify, request
from clarifai.rest import ClarifaiApp
from eco import eco

clarifai_app = ClarifaiApp(api_key='528ab75695b14ffe8be3e86f918f455a')


def calc_index(items):
    return eco.tree_predict(items)


def get_ingredients(url:str):
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


def calculate_stats(list):
    return list


app = Flask(__name__)
@app.route('/image', methods=['GET'])
def get_image():
    image_url = request.args.get('url')
    status, items, info = get_ingredients(image_url)
    return jsonify({'status': status, 'data': {'ingredients': items, 'information': info}})


@app.route('/stats', methods=['GET'])
def get_stats():
    list = request.args.get('list')
    return jsonify({'prediction': calculate_stats(list)})


if __name__ == '__main__':
    app.run()