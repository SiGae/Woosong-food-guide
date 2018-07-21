import pymysql
import WLB.dbconnect


def collect_user(userkey):
    db = pymysql.connect(host='sigae.me', port=3306, user=WLB.dbconnect.id, passwd=WLB.dbconnect.pw,
                         db=WLB.dbconnect.db, charset='UTF8')
    try:
        sql = "select username from userinfo"
        cursor = db.cursor()
        cursor.execute(sql)
        db.commit()

        row = cursor.fetchall()
        print(row)
        new_row = list(row)
        check = 0
        count = 0

        for i in new_row:
            print(i)
            if i[count] == userkey:
                check = 1
                break

        if check == 1:
            sql1 = "UPDATE userinfo SET usecount = usecount + 1 where username = '%s'" % userkey
        else:
            sql1 = "INSERT into userinfo(username, usecount) VALUES ('%s', 1)" % userkey

        cursor.execute(sql1)
        db.commit()
    finally:
        db.close()

    return

