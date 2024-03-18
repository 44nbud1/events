# Event Booking
This is technical document for event booking. 
![system.png](doc%2Fsystem.png)
## ERD
![erd.png](doc%2Ferd.png)

## Sequence Diagram
### Create book
![create-book.png](doc%2Fcreate-book.png)
https://www.plantuml.com/plantuml/uml/fLMnSjim3Dtv5HZJ8PVhgg6N9jUfI-_QeIjCmZPF4Yc3a1l_VIM6CIWxkdFJnPO0FF2zTn3uc0HPnhupgVK1E2Oo1gr4Xlil88mX3P6bWUFd1npymoR6DXcpYIz0HmfYXWdkhHymY9OleZRsFGQtY2NYexUqY34DCNc9V38E1JUO29YSJ_hQdzAXqrVgC4rmSnXf96Xnsw8n51nCzB8AVqIXYjRvQHPNU3Nmp8H2JvEoNzws9C-lYRjFsk9DG_bVmEsURFi36Nku2r701qWs3bJ1YzmQNa8J_7owCIKuvzMD1hO-kAVJTpffnyeiVPnnsQBt8CMy_5iLJjvDFTx25mSpoSW1PiEDeujEzRagQ0rKCpRO2VXjBe7PicqSWrFrzxyXZs1J9IcuLNzs93_jdjpOKH7-BS_cpMwjDxZ1kt-oJai-PkX7dIkuzARI1s0CEu8rJnC9zFW21tb_wkgWy-d2rnnxRIBK3tENiYzCJjrYb_R-Zr7BudSSnm6wQDlxl07cmXXqd9JmN0DrpPA9abSELv1h-jVdEB9CFGN9piHjCQTkNqkyXGTJHvWeyxdcK0y1CKU63LaS4y5NUnWzauFDH4Jsn21x9jIXkc1JJAtJUVCsaAOuz9GIxaW9vwHMLvkpQ3j37AqlpFeoMWVgebwi-l9DhCEqjp6PhtxaPJz3e3ymNtwQBxTF0-FWCcUfd5VbZnJSs7T_0G00

### Expiration handling
![recover-from-ttl-redis-expiratuion.png](doc%2Frecover-from-ttl-redis-expiratuion.png)
https://www.plantuml.com/plantuml/uml/XP11SiCW34NtEeKka0juqScckovp0HdKZiPOs20yoUrhqr0psMGZ6_ZpdgHhDafMvW1v4iKKCmCazf8_xuliFxVR5ru-SABlYGRauv9CecAM7wK0CCOxyiPgi1Ok_9U4PQUAauNKC7FQn35uCXefCrR-UPmVUGtdaGDb4mV0wd6d0v0psSWQkzQzgGOUi2p-U3uG0C_R8r150ugwn3EhuMgFHcjjr8gN7fVOIbBm_1BjqRNfV_COurJrQ157HaVgE0JspTQchFMThOPwMzj9y_Lpd0jxVj_HM_pAwiiSVW40

## API 
### Create book
Request
POST /v1/api/books
```json
{
  "key" :"Con10124",
  "groupKeys" : [
    {
      "key" : "gold-Con10124",
      "qty" : 3
    },
    {
      "key" : "sil-Con10124",
      "qty" : 5
    }
  ]
}
```

Response
```json
{
    "data": {
        "bookId": "49801592",
        "price": 40000000,
        "currency": "IDR"
    }
}
```
### Find event filter by name

GET /v1/api/events?name={name}

Response
```json
{
    "data": [
        {
            "id": 1,
            "name": "Blink 182 Goes to Indonesia",
            "key": "Con10124",
            "location": {
                "venue": "Jakarta Convention Center",
                "address": "JCC, Jl. Gatot Subroto, Senayan, Kecamatan Tanah Abang, Kota Jakarta Pusat, Daerah Khusus Ibukota Jakarta 10270"
            },
            "date": "2024-11-11T14:00:00.000+00:00",
            "type": "Concert",
            "eventGroups": [
                {
                    "id": 4,
                    "key": "sil-Con10124",
                    "category": "Silver",
                    "price": 5000000.0,
                    "currency": "IDR",
                    "startDate": "2024-08-04T13:00:00.000+00:00",
                    "endDate": "2024-07-04T12:00:00.000+00:00",
                    "qty": 28455
                },
                {
                    "id": 1,
                    "key": "pla-Con10124",
                    "category": "Platinum",
                    "price": 7000000.0,
                    "currency": "IDR",
                    "startDate": "2024-10-04T13:00:00.000+00:00",
                    "endDate": "2024-11-04T13:00:00.000+00:00",
                    "qty": 55000
                },
                {
                    "id": 2,
                    "key": "gold-Con10124",
                    "category": "Gold",
                    "price": 5000000.0,
                    "currency": "IDR",
                    "startDate": "2024-09-04T13:00:00.000+00:00",
                    "endDate": "2024-10-04T12:00:00.000+00:00",
                    "qty": 61052
                }
            ]
        }
    ]
}
```
### Temp create event
Request
POST /v1/api/events

Response
```json
{
    "data": "Success"
}
```
![localtest.png](doc%2Flocaltest.png)
Based on test above with 100 virtual users in rang 1 minutes, we notice that. 
![test.png](doc%2Ftest.png)

```
A. When there is request get the lock, the resource will be block.
B. Another thread try to take the same resource, but the resource still locked by other thread. 
C. The thread finish do something and release the lock.
D. After retry until max retry 5 times, we will recover the request.
```
