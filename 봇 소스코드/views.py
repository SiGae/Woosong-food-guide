from django.shortcuts import render
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt
import json
import random
import WLB.foodselecter
import WLB.collectuser

# Create your views here.



food_field = ['한식', '중식', '일식', '양식', '패스트푸드', '랜덤']


def keyboard(request):
    return JsonResponse(
        {
            'type': 'buttons',
            'buttons': food_field
        }
    )


@csrf_exempt
def message(request):
    json_str = request.body.decode('utf-8')
    received_json = json.loads(json_str)
    content_name = received_json['content']
    user_name = received_json['user_key']

    if content_name in food_field:
        WLB.collectuser.collect_user(user_name)
        if content_name == '랜덤':
            while content_name == '랜덤':
                content_name = random.choice(food_field)
        result = WLB.foodselecter.select_food(content_name)
        return JsonResponse({
            'message': {
                'text': '이름 : {0}\n가격 : {1}\n종류 : {2}\n식당명 : {3}\n전화번호 : {4}'
                        ''.format(result['name'], result["price"], result["field"], result["store"], result["phone"])
            },
            'keyboard': {
                'type': 'buttons',
                'buttons': ['한식', '중식', '일식', '양식', '패스트푸드', '랜덤']
            }
        })
    return JsonResponse({
            'message': {
                'text': '드시고 싶은 점심의 종류를 골라주세요'
            },
            'keyboard': {
                'type': 'buttons',
                'buttons': ['한식', '중식', '일식', '양식', '패스트푸드', '랜덤']
            }
        })