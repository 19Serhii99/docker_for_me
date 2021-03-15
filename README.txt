cd /c/Users/Administrator/Documents/"NIX Projects"/nesterov/25_docker
MSYS_NO_PATHCONV=1 docker run -i --rm --name 25_docker -v "$(pwd)":"$(pwd)" -w "$(pwd)" maven:latest mvn clean install
