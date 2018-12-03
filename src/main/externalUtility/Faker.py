from faker import Faker

fake=Faker(locale="en_US")

for i in range(1,500):
    print("\""+fake.password()+"\",")