import pymysql
import WLB.dbconnect


def NumberSlice(number) -> str:
    return format(number, ",")

def select_food(foodField):
    db = pymysql.connect(host='sigae.me', port=3306, user=WLB.dbconnect.id, passwd=WLB.dbconnect.pw,
                         db=WLB.dbconnect.db, charset='UTF8')

    try:

        first_sql = '''SELECT storename from woosongfood where field = '%s' ORDER BY RAND() LIMIT 1''' % foodField


        cursor = db.cursor()
        cursor.execute(first_sql)
        db.commit()

        result0 = list(cursor.fetchone())

        second_sql = '''SELECT menu, field, price, storename, phone FROM woosongfood join storeinfo on woosongfood.storename = storeinfo.name WHERE storename = '%s' ORDER BY RAND() LIMIT 1''' % result0[0]

        cursor.execute(second_sql)

        result1 = list(cursor.fetchone())
        print(result1)

        final_result = {
            'name': result1[0],
            'price': NumberSlice(result1[2]),
            'field': result1[1],
            'store': result1[3],
            'phone': " " + result1[4]
        }
    finally:
        db.close()
    return final_result
