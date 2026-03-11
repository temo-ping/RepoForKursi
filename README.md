
1. ბაზის გაშვება
პირველ რიგში გაუშვით PostgreSQL Docker Compose-ის მეშვეობით: docker-compose up -d

2. პროექტის აგება: mvn clean package

3. აპლიკაციის გაშვება java -jar target/van-loading-optimiser-0.0.1-SNAPSHOT.jar

4. აპლიკაციის გაშვების შემდეგ API დოკუმენტაცია ხელმისაწვდომია შემდეგ მისამართზე: http://localhost:8080/swagger-ui/index.html

5.ბაზის კონფიგურაცია
პროექტი იყენებს PostgreSQL ბაზას შესაბამისად არის დაწერილი როგორც application.properties ში ასევე compose ფაილში ხოლო მისი პარამეტრები არის:
Database name: vanloading
Username: postgres
Password: postgres123
Port: 5432



API ENDPOINTS:
1. ოპტიმიზაციის შექმნა: POST /api/v1/optimizations

curl -X POST http://localhost:8080/api/v1/optimizations \
  -H "Content-Type: application/json" \
  -d '{
    "maxVolume": 15,
    "availableShipments": [
     {
        "name": "Parcel A",
        "volume": 5,
        "revenue": 120
      },
      {
        "name": "Parcel B",
        "volume": 10,
        "revenue": 200
      },
      {
        "name": "Parcel C",
        "volume": 3,
        "revenue": 80
      },
      {
        "name": "Parcel D",
        "volume": 8,
        "revenue": 160
        
      }
    ]
  }'

პასუხის მაგალითი:

{
  "requestId": "3f29e2f9-6a33-4e2a-97d2-7efb2dd8c1c4",
  "selectedShipments": [
    {
      "name": "Parcel A",
      "volume": 5,
      "revenue": 120
    },
    {
      "name": "Parcel B",
      "volume": 10,
      "revenue": 200
    }
    
  ],
  "totalVolume": 15,
  "totalRevenue": 320,
  "createdAt": "2026-03-11T12:00:00Z"

}


2.ოპტიმიზაციის წამოღება ID-ით: GET /api/v1/optimizations/{requestId}

curl http://localhost:8080/api/v1/optimizations/3f29e2f9-6a33-4e2a-97d2-7efb2dd8c1c4

პასუხის მაგალითი:
{
  "requestId": "3f29e2f9-6a33-4e2a-97d2-7efb2dd8c1c4",
  "selectedShipments": [
    {
      "name": "Parcel A",
      "volume": 5,
      "revenue": 120
    },
    {
      "name": "Parcel B",
      "volume": 10,
      "revenue": 200
    }
    
  ],
  "totalVolume": 15,
  "totalRevenue": 320,
  "createdAt": "2026-03-11T12:00:00Z"
}



3.ყველა ოპტიმიზაციის წამოაეღება: GET /api/v1/optimizations

curl http://localhost:8080/api/v1/optimizations

[
  {
    "requestId": "3f29e2f9-6a33-4e2a-97d2-7efb2dd8c1c4",
    "selectedShipments": [
      {
        "name": "Parcel A",
        "volume": 5,
        "revenue": 120
      },
      {
        "name": "Parcel B",
        "volume": 10,
        "revenue": 200
      }
      
    ],
    "totalVolume": 15,
    "totalRevenue": 320,
    "createdAt": "2026-03-11T12:00:00Z"
  }
]



მონაცემთა ბაზის სქემა:

პროექტში გამოყენებულია ორი ცხრილი

1.ცხირილი

optimization_requests

ეს ცხრილი ინახავს თითოეული ოპტიმიზაციის ძირითად ინფორმაციას:

id,
max_volume,
total_volume,
total_revenue,
created_at

2.ცხრილი
selected_shipments

ეს ცხრილი ინახავს იმ shipment-ებს, რომლებიც კონკრეტულ ოპტიმიზაციაში იქნა არჩეული:

id
shipment_name
volume
revenue
optimization_request_id


კავშირი ცხრილებს შორის

optimization_requests და selected_shipments დაკავშირებულია one-to-many კავშირით:

ერთ optimization request-ს შეიძლება ჰქონდეს მრავალი selected shipment
თითოეული selected shipment ეკუთვნის მხოლოდ ერთ optimization request-ს



ასეთი დიზაინი საშუალებას იძლევა:

ცალკე შევინახოთ ოპტიმიზაციის summary
ცალკე შევინახოთ არჩეული shipment-ების დეტალები
მარტივად მოვძებნოთ ერთი ოპტიმიზაცია ან ოპტიმიზაციების ისტორია

ინდექსები არსებობს მხოლოდ primary key ველებზე, რომლებიც ავტომატურად იქმნება მონაცემთა ბაზის მიერ:

optimization_requests.id
selected_shipments.id

ეს ინდექსები უზრუნველყოფს ჩანაწერების სწრაფ მოძებნას
