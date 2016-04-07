#Interconnecting Flights Search

[![Build Status](https://semaphoreci.com/api/v1/arturopala/spring-boot-flights-search/branches/master/badge.svg)](https://semaphoreci.com/arturopala/spring-boot-flights-search)

Spring MVC based RESTful API application which serves information about possible direct
and interconnected flights (maximum 1 stop) based on the data consumed from external APIs.

##API

###GET `/interconnections?departure={departure}&arrival={arrival}&departureDateTime={departureDateTime}&arrivalDateTime={arrivalDateTime}`
-   departure - a departure airport IATA code
-   departureDateTime - a departure datetime in the departure airport timezone in ISO format
-   arrival - an arrival airport IATA code
-   arrivalDateTime - an arrival datetime in the arrival airport timezone in ISO format

For example:
<http://localhost:8080/{context}/interconnections?departure=DUB&arrival=WRO&departureDateTime=2016-03-01T07:00&arrivalDateTime=2016-03-03T21:00>
        
The application returns a list of flights departing from a given departure airport not earlier
than the specified departure datetime and arriving to a given arrival airport not later than the
specified arrival datetime. 

The list consist of:
-   all direct flights if available (for example: DUB - WRO)
-   all interconnected flights with a maximum of one stop if available (for example: DUB - STN -
WRO)
-   For interconnected flights the difference between the arrival and the next departure should be 2h
or greater

Example response:

```json
[
  {
    "stops": 0,
    "legs": [
      {
        "departureAirport": "DUB",
        "arrivalAirport": "WRO",
        "departureDateTime": "2016-05-02T09:45",
        "arrivalDateTime": "2016-05-02T13:20"
      }
    ]
  },
  {
    "stops": 0,
    "legs": [
      {
        "departureAirport": "DUB",
        "arrivalAirport": "WRO",
        "departureDateTime": "2016-05-04T17:50",
        "arrivalDateTime": "2016-05-04T21:25"
      }
    ]
  },
  {
    "stops": 0,
    "legs": [
      {
        "departureAirport": "DUB",
        "arrivalAirport": "WRO",
        "departureDateTime": "2016-05-05T19:30",
        "arrivalDateTime": "2016-05-05T23:05"
      }
    ]
  },
  {
    "stops": 0,
    "legs": [
      {
        "departureAirport": "DUB",
        "arrivalAirport": "WRO",
        "departureDateTime": "2016-05-06T18:00",
        "arrivalDateTime": "2016-05-06T21:35"
      }
    ]
  },
  {
    "stops": 0,
    "legs": [
      {
        "departureAirport": "DUB",
        "arrivalAirport": "WRO",
        "departureDateTime": "2016-05-08T13:55",
        "arrivalDateTime": "2016-05-08T17:30"
      }
    ]
  },
  {
    "stops": 1,
    "legs": [
      {
        "departureAirport": "DUB",
        "arrivalAirport": "GLA",
        "departureDateTime": "2016-05-02T07:20",
        "arrivalDateTime": "2016-05-02T08:20"
      },
      {
        "departureAirport": "GLA",
        "arrivalAirport": "WRO",
        "departureDateTime": "2016-05-02T11:45",
        "arrivalDateTime": "2016-05-02T15:15"
      }
    ]
  },
  ...
]
```
## Development

### Build & Test

`mvn clean test`

### Run locally

`mvn clean spring-boot:run`

Open <http://localhost:8080/jsondoc-ui.html>

Paste `http://localhost:8080/jsondoc` into search field and click [*Enter*]

### Build war archive

`mvn clean package`
