#Fusionauth microservice

##Instalation
First create a network 

```
sudo docker network create videoteka_network
```
To run this microservice just do 
```
sudo docker-compose up 
```

It has its kickstart when the app is building up it will automatically insert 
tenantId, appId (with name), roles for the user inside that app and it will create
the universal api key for all tenants.

To get the jwt secret go to IP_address:9011, login, and go to settings -> Key master -> Default signing key (on the right there is a magnifying glass) click it, and in secret click `chlick here` so you can get the jwt secret
this secret is important, because it needs to be added to the other application (Videoteka)