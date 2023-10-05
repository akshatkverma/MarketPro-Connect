# API

This document provides a concise description of the APIs developed for **Market-Pro Connect** by the team **Lifetime Errors**



## Registration Endpoint
This API allows users to register for your application. It provides a way for new users to create an account by providing their necessary information.

### POST /api/register

>### Case 1 : Request OTP 
>>**Description** : Use this version of the endpoint to request otp while registering.
>>
>>#### Request
>>- **Method**: `POST`
>>- **URL**: `/api/auth/register`
>>- **Headers**:
>>   - `Content-Type: application/json`
>>#### Request Body
>>```json
>>{
>>    send_otp: true
>>    email: "dummyemail@gmail.com",
>>}
>>```
>>
>### Case 2 : Validate OTP and Register User
>>**Description** : Use this version of the endpoint to register user once OTP is successfully sent.
>>
>>#### Request
>>- **Method**: `POST`
>>- **URL**: `/api/auth/register`
>>- **Headers**:
>>   - `Content-Type: application/json`
>>#### Request Body
>>```json
>>{
>>    verify_and_register : true
>>    email: "dummyemail@gmail.com",
>>    otp: "123456",
>>    name: "dummy",
>>    password: "dummy_password",
>>    confirm_password: "dummy_password"
>>}
>>```
>>#### Response Body on Success
>>```json
>>{
>>     "message": "Registered Successfully",
>>     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0aW1lIjoiVGh1IE9jdCAwNSAyMDIzIDExOjUyOjM5IEdNVCswNTMwIChJbmRpYSBTdGFuZGFyZCBUaW1lKSIsInVzZXJfaWQiOiI2NTFlNTViNTYzNWQxZmNlODBhMTAzNmUiLCJpYXQiOjE2OTY0ODY5NTksImV4cCI6MTY5OTA3ODk1OX0.35qdlSX5XswSYbWw7TcgTTemvp3XXStmOiRW6mojOKo"  
>>}
>>```
>>                             
>

## Login Endpoint
This API allows users to login for your application.

### POST /api/auth/login
>**Description** : Use this version of the endpoint to register user once OTP is successfully sent.
>
>#### Request
>- **Method**: `POST`
>- **URL**: `/api/auth/login`
>- **Headers**:
>   - `Content-Type: application/json`
>#### Request Body
>```json
>{
>    email: "dummyemail@gmail.com",
>    password: "dummy_password",
>}
>```
>#### Response Body on Success
>```json
>{
>     "message": "Login Successful",
>     "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0aW1lIjoiVGh1IE9jdCAwNSAyMDIzIDExOjUyOjM5IEdNVCswNTMwIChJbmRpYSBTdGFuZGFyZCBUaW1lKSIsInVzZXJfaWQiOiI2NTFlNTViNTYzNWQxZmNlODBhMTAzNmUiLCJpYXQiOjE2OTY0ODY5NTksImV4cCI6MTY5OTA3ODk1OX0.35qdlSX5XswSYbWw7TcgTTemvp3XXStmOiRW6mojOKo"  
>}
>```
>                             
>

