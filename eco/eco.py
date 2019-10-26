# access via import eco.eco as eco

DATA = {
    "beverage": {
        "BEER": {"nut" : [0.66575, 9.68493, 153, 6.3, 0, 50.6, 0, 14.2, 0]},
        "COFFEE": {"nut" : [0.42466, 140, 1, 0, 0, 0, 0, 0, 0]},
        "MILK": {"nut" : [0.6274, 125.29589, 150, 8, 5, 12, 12, 116, 20]},
        "TEA": {"nut" : [0.0411, 52, 0, 0, 0, 0, 0, 9.5, 0]},
        "WINE": {"nut" : [0.31233, 13.76986, 25, 0.1, 0, 1.2, 1.2, 1.5, 0]}
    },
    "fruit": {
        # "ITEM": [CO2, Water, Calories, Protein, Fat, Carbohydrates, Sugar, Sodium, Cholesterol]
        "APPLE": {"nut": [0.03288, 14.36986, 65, 1.8, 1.8, 17.3, 13, 1.3, 0]},
        "AVOCADO": {"nut" : [0.19726, 44.98904, 384, 15.1, 293, 72.3, 0.7, 18.4, 0]},
        "BANANA": {"nut" : [0.06849, 9.13425, 200, 8.2, 6.2, 186, 27.5, 2.3, 0]},
        "BERRY": {"nut" : [0.12055, 33.47671, 85, 1.1, 0.5, 21, 15, 1, 0]},
        "CITRUS FRUIT": {"nut" : [0.03014, 14.0274, 60, 1, 0, 15, 11, 0, 0]},
        "GRAPE": {"nut" : [0.12055, 33.47671, 62, 0.6, 0.3, 16, 15, 2, 0]},
    },
    "grain": {
        "BREAD": {"nut" : [0.05753, 24.64384, 69, 3, 1, 12, 1.6, 120, 0]},
        "OATMEAL": {"nut" : [0.10411, 20.55616, 394, 11, 6.4, 73, 12, 21, 0]},
        "PASTA": {"nut" : [0.11781, 57.52055, 338, 12, 1.4, 68, 2.4, 5.5, 0]},
        "RICE": {"nut" : [0.33151, 168.16712, 199, 4.2, 0.4, 45, 0, 6.6, 0]},
    },
    "protein": {
        "BEEF": {"nut" : [7.72603, 16.50137, 217, 26.2, 11.8, 0, 0, 140, 77]},
        "STEAK": {"nut" : [7.72603, 16.50137, 217, 26.2, 11.8, 0, 0, 140, 77]},
        "CHICKEN": {"nut" : [1.36164, 91.21644, 143, 17, 8.1, 0, 0, 60, 86]},
        "EGG": {"nut" : [0.55342, 68.79452, 78, 6, 5, 0.6, 0.6, 62, 187]},
        "FISH": {"nut" : [1.87123, 76, 82, 15.7, 1.7, 9, 0, 37, 56]},
        "SALMON": {"nut" : [1.87123, 76, 82, 15.7, 1.7, 9, 0, 37, 56]},
        "LAMB": {"nut" : [4.33425, 16, 258, 25.6, 16.5, 0, 0, 61, 82]},
        "PORK": {"nut" : [1.79726, 262.34521, 252, 22, 18, 0, 0, 62, 80]},
        "PRAWN": {"nut" : [3.4411, 450.53699, 85, 20, 0.5, 0, 0, 119, 161]},
        "SHRIMP": {"nut" : [3.4411, 450.53699, 85, 20, 0.5, 0, 0, 119, 161]},
        "TOFU": {"nut" : [0.1589, 7.50137, 52, 5.8, 2.3, 2, 1.1, 30, 0]},
    },
    "spread": {
        "CHEESE": {"nut" : [0.96438, 226.58356, 390, 27, 31, 1.4, 0, 187, 93]},
        "CHOCOLATE": {"nut" : [1.25479, 48.60274, 579, 6.1, 38, 52, 37, 10, 6]},
    },
    "vegetable": {
        "BEAN": {"nut" : [0.09863, 24.35068, 31, 1.8, 0.2, 7, 3.3, 6, 0]},
        "LETTUCE": {"nut" : [0.01426, 10, 0.6, 0.1, 0.1, 2.1, 1.4, 7, 0]},
        "NUT": {"nut" : [0.0137, 133.30411, 607, 21, 55, 18, 4.6, 143, 0]},
        "ONION": {"nut" : [0.116, 27, 39.67, 1.2, 0.1, 10, 4.7, 4, 0]},
        "PEA": {"nut" : [0.01644, 24.35068, 81, 5.4, 0.4, 14, 5.7, 5, 0]},
        "POTATO": {"nut" : [0.04384, 17, 77, 2.1, 0.1, 17, 0.8, 6, 0]},
        "SALAD": {"nut" : [0.15, 56.7812, 18, 0.9, 0.2, 3.7, 1.9, 10, 0]},
        "TOMATO": {"nut" : [0.16438, 29.50411, 18, 1, 0.1, 4, 2.5, 11, 0]},
    }
}

HEALTH = {
    "calorie": 700,
    "protien": 17,
    "fat": 20,
    "carbohydrate": 92,
    "sugar": 10,
    "sodium": 500,
    "cholesterol": 100
}


def _cap(ingredients):
    for i, ing in enumerate(ingredients):
        ingredients[i] = ing.upper()
    return ingredients


def _new_ing(ingredients):
    ingredients1 = []
    for i, ing in enumerate(ingredients):
        for cat in DATA:
            for t in DATA[cat]:
                if ing in t or t in ing:
                    ingredients1.append(t)
                    ingredients.pop(i)
    return ingredients1


def _reco(food):
    for cat in DATA:
        if food in DATA[cat]:
            w_co2 = DATA[cat][food]["nut"][0]
            for t in DATA[cat]:
                if DATA[cat][t]["nut"][0] < w_co2:
                    b_co2 = t
    return b_co2


def _health_grade(ingredients):
    val = 0
    for ing in ingredients:
        for j, atr in enumerate(HEALTH):
            for cat in DATA:
                if ing in DATA[cat]:
                    diff = abs(HEALTH[atr] - DATA[cat][ing]["nut"][j]) / (HEALTH[atr] + DATA[cat][ing]["nut"][j]) / 2
                    if diff >= .1:
                        val += 1/14
    return val


def food_quality(ingredients):
    
    # return object
    use = {
        "emission": 0,
        "water": 0,
        "health": 5,
        "tree": 0,
        "rec": "",
    }
    
    # capitalize ingredients
    ingredients = _cap(ingredients)
    
    # format ingredients
    ingredients = _new_ing(ingredients)

    # emission max
    e_max = 0
    
    # algorithm application
    for ing in ingredients:
        for cat in DATA:
            if ing in DATA[cat]:
                nutr = DATA[cat][ing]["nut"]
                use["emission"] += nutr[0]
                use["water"] += nutr[1]
                use["tree"] += (nutr[0] + nutr[1]) / 1960 # tree mass = 1960 kg
                if nutr[0] > e_max:
                    e_max = nutr[0]
                    use["rec"] = ing

    # better food option
    use["rec"] = _reco(use["rec"])

    # health grade calculation
    use["health"] -= _health_grade(ingredients)

    if use["health"] >= 4:
        use["health"] = 'A'
    elif use["health"] >= 3:
        use["health"] = 'B'
    elif use["health"] >= 2:
        use["health"] = 'C'
    elif use["health"] >= 1:
        use["health"] = 'D'
    else:
        use["health"] = 'F'

    return use

####

def _mean(lst):
    mean = sum(lst) / len(lst)
    return mean

def _linregress(x, y):
    m = (((_mean(x) * _mean(y)) - _mean([a * b for a, b in zip(x, y)])) / ((_mean(x) ** 2) - _mean([a**2 for a in x])))
    b = _mean(y) - m * _mean(x)
    return [m, b]

def tree_predict(co2):

    for i in range(len(co2)):
        co2[i] = 60 - co2[i]
    
    x = [i for i in range(len(co2))]
    line = _linregress(x, co2)

    trees = int(sum([line[0] * i + line[1] for i in range(365)]) // 980)

    return trees if trees > 0 else 0