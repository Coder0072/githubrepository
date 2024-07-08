import sqlite3
class Database:
    def __init__(self,db):
        self.con=sqlite3.connect(db)
        self.curs=self.con.cursor()
        sql="""
        CREATE TABLE IF NOT EXISTS patients(id integer primary key,
        name text,age text,dob text,email text,gender text,contact text,address text
        )
        """
        self.curs.execute(sql)
        self.con.commit()
        #insert function
    def insert(self,name,age,dob,email,gender,contact,address):
        self.curs.execute("insert into patients values(NULL,?,?,?,?,?,?,?)",
                          (name,age,dob,email,gender,contact,address))
        self.con.commit()
        #fetch all from db
    def fetch(self):
        self.curs.execute("SELECT * from patients")
        rows=self.curs.fetchall()
        #print(rows)
        return (rows)
    #delete a record in db
    def remove(self,id):
        self.curs.execute("delete from patients where id=?",
                          (id,))
        self.con.commit()
    #update a record in db
    def update(self,id,name,age,dob,email,gender,contact,address):
        self.curs.execute("update patients set name=?,age=?,dob=?,email=?,gender=?,contact=?,address=? where id=?",
                          (name, age, dob, email, gender, contact, address,id))
        self.con.commit()



