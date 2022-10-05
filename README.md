# SecurityEmail
## Getting started
* Use command git clone https://github.com/MaksymHryhorov/SecurityEmail.git
* Configure your mail properties and database in application.properties file
* Start the application
* Open API platform. I use Postman for example
* Create post query

![image](https://user-images.githubusercontent.com/84277122/194052379-07a4b028-4873-4482-8e2d-a0d935ba1e08.png)

* If you don't fill fields correctly, you will see http status - bad request

![image](https://user-images.githubusercontent.com/84277122/194052622-eafaccab-c935-452c-84aa-2c1407307cf0.png)

* After you send post query you will receive your confrimation token

![image](https://user-images.githubusercontent.com/84277122/194053110-9921bd09-2597-4e7b-b3a7-85ed08091897.png)

Comment : token will expire after 15 minutes

* After that you will be registered in the database

![image](https://user-images.githubusercontent.com/84277122/194053471-8ed49b07-da9b-4cf5-87be-015556e2ac50.png)

* But you won’t be able to log in until you confirm it via email. You will see this message

![image](https://user-images.githubusercontent.com/84277122/194053827-79af05d3-7d41-4d9a-853e-7aa2e4788fdb.png)

* Let’s go to our email and will confirm the account

![image](https://user-images.githubusercontent.com/84277122/194054268-3a93e4ed-8f60-4d24-8a79-8fb1f4f2549f.png)

* Now we can log in by mail. After successfully log in you will see this page

![image](https://user-images.githubusercontent.com/84277122/194054486-54e09811-c02c-4b0e-b45a-2e66b4aac6e1.png)
