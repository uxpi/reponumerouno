./mvnw -DskipTests package

docker build -t gcr.io/axiomatic-lamp-176915/speed:v4 . 

gcloud docker -- push gcr.io/axiomatic-lamp-176915/speed:v4

kubectl set image deployment/speed speed=gcr.io/axiomatic-lamp-176915/speed:v4