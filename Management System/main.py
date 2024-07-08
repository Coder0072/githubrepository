from tkinter import *
from tkinter import ttk
from tkinter import messagebox
from DB import Database
DB=Database("Patient.DB")

root=Tk()
root.title("Patient Management System")
root.geometry('1366x768+0+0')
root.config(bg="#34568B")
root.state('zoomed')
name=StringVar()
age=StringVar()
dob=StringVar()
gender=StringVar()
email=StringVar()
contact=StringVar()

#entry frame
entries_frame=Frame(root,bg='#535c68')
entries_frame.pack(side=TOP,fill=X)
title=Label(entries_frame,text="Patient management system",font=("Calibri",18,"bold"),bg="#535c68",fg='white')
title.grid(row=0,columnspan=2)
lblName=Label(entries_frame,text="Name",font=("calibri",16),bg="#535c68",fg='white')
lblName.grid(row=1,column=0,padx=10,pady=10,sticky='w')
txtName=Entry(entries_frame,textvariable=name,font=("calibri",16),width=30)
txtName.grid(row=1,column=1,padx=10,pady=10,sticky='w')

lblAge=Label(entries_frame,text="Age",font=("calibri",16),bg="#535c68",fg='white')
lblAge.grid(row=1,column=2,padx=10,pady=10,sticky='w')
txtAge=Entry(entries_frame,textvariable=age,font=("calibri",16),width=30)
txtAge.grid(row=1,column=3,padx=10,pady=10,sticky='w')

lblDOB=Label(entries_frame,text="DOB",font=("calibri",16),bg="#535c68",fg='white')
lblDOB.grid(row=2,column=0,padx=10,pady=10,sticky='w')
txtDOB=Entry(entries_frame,textvariable=dob,font=("calibri",16),width=30)
txtDOB.grid(row=2,column=1,padx=10,pady=10,sticky='w')

lblEmail=Label(entries_frame,text="Email",font=("calibri",16),bg="#535c68",fg='white')
lblEmail.grid(row=2,column=2,padx=10,pady=10,sticky='w')
txtEmail=Entry(entries_frame,textvariable=email,font=("calibri",16),width=30)
txtEmail.grid(row=2,column=3,padx=10,pady=10,sticky='w')

lblGender=Label(entries_frame,text="Gender",font=("calibri",16),bg="#535c68",fg='white')
lblGender.grid(row=3,column=0,padx=10,pady=10,sticky='w')
comboGender=ttk.Combobox(entries_frame,font=("calibri",16),width=28,textvariable=gender,state='readonly')
comboGender['values']=['Male','Female','Transgender']
comboGender.grid(row=3,column=1,padx=10,sticky='w')

lblContact=Label(entries_frame,text="Contact",font=("calibri",16),bg="#535c68",fg='white')
lblContact.grid(row=3,column=2,padx=10,pady=10,sticky='w')
txtContact=Entry(entries_frame,textvariable=contact,font=("calibri",16),width=30)
txtContact.grid(row=3,column=3,padx=10,pady=10,sticky='w')

lblAddress=Label(entries_frame,text="Address",font=("calibri",16),bg="#535c68",fg='white')
lblAddress.grid(row=4,column=0,padx=10,pady=10,sticky='w')

txtAddress=Text(entries_frame,width=85,height=5,font=("calibri",16))
txtAddress.grid(row=5,column=0,columnspan=4,padx=10,sticky='w')

def getData(event):
    selected_row = tv.focus()
    data=tv.item(selected_row)
    global row
    row=data["values"]
    name.set(row[1])
    age.set(row[2])
    dob.set(row[3])
    email.set(row[4])
    gender.set(row[5])
    contact.set(row[6])
    txtAddress.delete(1.0,END)
    txtAddress.insert(END,row[7])
def displayAll():
    tv.delete(*tv.get_children())
    for row in DB.fetch():
        tv.insert("",END,values=row)

def add_patient():
    if (txtName.get()==""or txtDOB.get()==""or txtEmail.get()==""or txtAge.get()==""or txtContact.get()==""or
        txtAddress.get(1.0,END)==""):
        messagebox.showerror('Error in Getting input','please fill the all Details...')
        return
    DB.insert(txtName.get(), txtDOB.get(), txtEmail.get(),comboGender.get(), txtAge.get() ,txtContact.get(),txtAddress.get(1.0,END))

def update_patient():
    if (txtName.get()==""or txtDOB.get()==""or txtEmail.get()==""or txtAge.get()==""or txtContact.get()==""or
        txtAddress.get(1.0,END)==""):
        messagebox.showerror('Error','Error...')
        return
    DB.update(row[0],txtName.get(), txtDOB.get(), txtEmail.get(),comboGender.get(), txtAge.get() ,txtContact.get(),txtAddress.get(1.0,END))
    messagebox.showerror('Updation process', 'Update Successfully...')


def delete_employee():
    DB.remove(row[0])
    clear_all()
    displayAll()
def clear_all():
    name.set("")
    age.set("")
    dob.set("")
    gender.set("")
    email.set("")
    contact.set("")
    txtAddress.delete(1.0,END)


btn_frame=Frame(entries_frame,bg="#535c68")
btn_frame.grid(row=6,column=0,columnspan=4,padx=10,sticky='w')
btnAdd=Button(btn_frame,command=add_patient,text='Add Details',width=15,font=('calibri',16,'bold'),fg='white',bg='#16a085',bd=0).grid(row=0,column=0)

btnEdit=Button(btn_frame,command=update_patient,text='Update Details',width=15,font=('calibri',16,'bold'),fg='white',bg='#2980b9',bd=0).grid(row=0,column=1,padx=10)

btnDelete=Button(btn_frame,command=delete_employee,text='Delete Details',width=15,font=('calibri',16,'bold'),fg='white',bg='#c0392b',bd=0).grid(row=0,column=2,padx=10)

btnClear=Button(btn_frame,command=clear_all,text='Clear Details',width=15,font=('calibri',16,'bold'),fg='white',bg='#f39c12',bd=0).grid(row=0,column=3,padx=10)

#Table Frame
tree_frame=Frame(root,bg='#ecf0f1')
tree_frame.place(x=0,y=480,width=1366,height=500)
Style=ttk.Style()
Style.configure("mystyle.Treeview",font=('calibri',18),rowheight=50)
Style.configure("mystyle.Treeview.Heading",font=('calibri',20,'bold'),rowheight=50)
tv=ttk.Treeview(tree_frame,column=(1,2,3,4,5,6,7,8))
tv.heading('1',text='ID')
tv.column('1',width=5)
tv.heading('2' ,text='Name')
tv.heading('3',text='Age')
tv.heading('4',text='D.O.B')
tv.heading('5',text='Gender')
tv.heading('6',text='Email')
tv.heading('7',text='Contact')
tv.heading('8',text='Address')
tv['show']='headings'
tv.bind("<ButtonRelease-1>",getData)
tv.pack(fill=X)
displayAll()
root.mainloop()