GET /v1/events?name=Blink

```json

{
  "responseCode" : "200",
  "responseMessage" : "Successful",
  "data" : [
    {
      "key": "12345567",
      "name": "Tour Blink 182 Jakarta - Day 1",
      "location": {
        "vanue": "Beach City International Stadium",
        "address": "Jl. Kw. Wisata Ancol, Ancol, Pademangan, North Jakarta City, Jakarta 14430"
      },
      "date": "2024-06-15T22:27:45+07:00",
      "type": "CONCERT",
      "eventGroups": [
        {
          "key": "101",
          "category": "Ultimate Experience",
          "price": 11000000.00,
          "currency": "IDR",
          "startDate": "2024-03-15T22:27:45+07:00",
          "endDate": "2024-05-15T22:27:45+07:00",
          "qty": 10000000
        },
        {
          "key": "102",
          "category": "My Universe (Festival)",
          "price": 5700000.00,
          "currency": "IDR",
          "startDate": "2024-03-15T22:27:45+07:00",
          "endDate": "2024-05-15T22:27:45+07:00",
          "qty": 5000000
        },
        {
          "key": "103",
          "category": "CAT 1 (Numbered Seating)",
          "price": 5000000.00,
          "currency": "IDR",
          "startDate": "2024-03-15T22:27:45+07:00",
          "endDate": "2024-05-15T22:27:45+07:00",
          "qty": 7000000
        }
      ]
    }
  ]
}
```

Request
POST /v1/books
```json
{
  "key" :"12345567",
  "groupKey" : [
    {
      "key" : "101",
      "qty" : 3
    },
    {
      "key" : "102",
      "qty" : 5
    }
  ]
}

```
Response
```json
{
  "responseCode": "200",
  "responseMessage": "Successful",
  "data": {
    "bookId" : "568920",
    "price" : 35000000,
    "currency" : "IDR"
  }
}
```

Request
POST /v1/payments
```json

{
  "bookId" : "568920",
  "method" : "VA", 
  "amount" : 35000000,
  "currency" : "IDR"
}
```

```json
{
  "responseCode": "200",
  "responseMessage": "Payment success", 
  "data" : {
    "tickets" : [
      {
        "key" : "12345567",
        "name" : "Tour Blink 182 Jakarta - Day 1",
        "location" : {
          "vanue" : "Beach City International Stadium",
          "address" : "Jl. Kw. Wisata Ancol, Ancol, Pademangan, North Jakarta City, Jakarta 14430"
        },
        "date" : "2024-06-15T22:27:45+07:00",
        "type" : "CONCERT",
        "eventGroups" : [
          {
            "ticketNo" : "787799799",
            "category" : "Ultimate Experience",
            "price" : 11000000.00,
            "currency" : "IDR"
          },
          {
            "ticketNo" : "787799798",
            "key" : "102",
            "category" : "My Universe (Festival)",
            "price" : 5700000.00
          },
          {
            "ticketNo" : "787799797",
            "key" : "103",
            "category" : "CAT 1 (Numbered Seating)",
            "price" : 5000000.00,
            "currency" : "IDR"
          }
        ]
      }
    ]
  }
}
```