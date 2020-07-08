# pp-tests

## Setup docker and run hello-service in the container

### Install Docker on your desktop
* Go to the Docker official site: https://www.docker.com/products/docker-desktop 
* Install the latest stable version depending on your operating system 
* After installation check that everything is ok:  
```docker -v```  
The output to your console should be something like this:   
```Docker version 19.03.8, build afacb8b```

### Create your own Dockerfile:
* Go to the DockerHub, where you can find officials Docker images: https://hub.docker.com/
* Find the 'alpine' image there and find the latest stable release (for example, 3.12). 
Alpine - it's a very light Docker image based on Alpine Linux system.
* Create a file called Dockerfile in the root directory of this project.
* Add the following lines to this file:   
```
   # determine the image that you will use
   FROM alpine:3.12
   
   # create a directory in which the binary file for starting the service will be stored
   RUN mkdir -p /usr/hello-service
   WORKDIR /usr/hello-service
   
   # copy linux x86-64 binary file 'hello' from dir 'src/test/resources' to the directory of your image '/usr/hello-service'
   COPY src/test/resources/hello /usr/hello-service
   
   # go to the directory in the image
   RUN cd /usr/hello-service
   
   # add command to run service
   ENTRYPOINT ["./hello"]
```

### Build your own docker image
* run the next command in the directory with your Dockerfile for create your image:   
```docker build -t hello-service -f Dockerfile .```
* Verify that the image was created successfully:   
```docker images``` 
* The output to your console should be something like this:   
```
REPOSITORY          TAG                 IMAGE ID            CREATED              SIZE
hello-service       latest              d321616f0b6c        About a minute ago   7.25MB
```
Congratulations! You created your own docker-image with hello-service inside. 

### Run your service in the container locally:
* Run the command:   
```docker run -p 8082:8080 hello-service```
* The output to your console should be:   
```2020/07/08 00:00:00 Listening on :8080```  
Using ```-p 8082:8080``` argument you can run your service with a specific port. 

### Call your service:
* Run the command on the another terminal window:   
```curl localhost:8082/peter```
* The output should be:   
```Hi there, peter!```

## Setup Kubernetes cluster and run hello-service into the cluster

### Install Minikube:
"Minikube is a tool that makes it easy to run Kubernetes locally. 
Minikube runs a single-node Kubernetes cluster inside a Virtual Machine (VM) on your laptop for users looking to try out Kubernetes or develop with it day-to-day." (c)

* Go to the next page: https://kubernetes.io/docs/tasks/tools/install-minikube/ 
* Install Minikube in accordance with the current instructions and depending on your operating system
* Confirm installation:   
```minikube version```   
The output to console should be like this:   
```
minikube version: v1.11.0
commit: 57e2f55f47effe9ce396cea42a1e0eb4f611ebbd
``` 

### Start Minikube with docker driver: 
* Run the command:   
```minikube start --driver=docker```   
Check the status:    
```minikube status```   
The output should be:   
```
minikube
type: Control Plane
host: Running
kubelet: Running
apiserver: Running
kubeconfig: Configured
```

### Describe hello-service using Kubernetes .yaml:
* Create 2 .yaml files in the root of this project with the next following names:  
**hello-deployment.yaml**   
**hello-service.yaml**   

* In the **hello-deployment.yaml** file define your docker-image and don't forget to add parameter ```imagePullPolicy: Never```  
 (this is so that the system does not download the image from the cloud but uses a local image). 
 See example of this file in this repository.

* In the **hello-service.yaml** file set up service name, type, ports and other parameters if necessary.  
See example of this file in this repository too.

* Set env variable for local env:  
```eval $(minikube docker-env)```

* Build the docker image with the Minikube’s Docker daemon:
```docker build -t hello-service .```

* Make sure in the file `hello-service.yaml`  
parameter `name: hello-service`  
and parameter `imagePullPolicy: Never`

### Deploy your service into the claster:
* Run the command:  
```kubectl apply -f hello-deployment.yaml```

* Run the command:  
```kubectl apply -f hello-service.yaml``` 

* Check everything is ok:  
```kubectl get pod```  
Output should be: 
```
NAME                                READY   STATUS    RESTARTS   AGE
hello-deployment-75f5d8568d-gqlct   1/1     Running   0          94s
```

* Check your service info:  
```kubectl get service```  
Output should be:  
```
NAME            TYPE        CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
hello-service   NodePort    10.100.31.103   <none>        8080:30001/TCP   2m23s
kubernetes      ClusterIP   10.96.0.1       <none>        443/TCP          12h
```

### Check your service in the cluster run successfully:
* Get service logs:
```kubectl logs hello-deployment-75f5d8568d-gqlct```  
Output should be:   
```2020/07/08 21:34:35 Listening on :8080``` - it means that **hello-service** started into the container.

* Get service info: 
```kubectl describe service hello-service```  
Output should be:  
```
Name:                     hello-service
Namespace:                default
Labels:                   <none>
Annotations:              kubectl.kubernetes.io/last-applied-configuration:
                            {"apiVersion":"v1","kind":"Service","metadata":{"annotations":{},"name":"hello-service","namespace":"default"},"spec":{"ports":[{"nodePort...
Selector:                 app=web
Type:                     NodePort
IP:                       10.100.31.103
Port:                     <unset>  8080/TCP
TargetPort:               8080/TCP
NodePort:                 <unset>  30001/TCP
Endpoints:                172.18.0.6:8080
Session Affinity:         None
External Traffic Policy:  Cluster
Events:                   <none>
```
In the output you can find endpoint and port (`172.18.0.6:30001`). 
This IP and PORT you can use to access to the service if it will be run on the cloud env.

### Send request to the service
* Run the command:  
```minikube service hello-service```  
The output should be next:  
```
|-----------|---------------|-------------|-------------------------|
| NAMESPACE |     NAME      | TARGET PORT |           URL           |
|-----------|---------------|-------------|-------------------------|
| default   | hello-service |        8080 | http://172.17.0.2:30001 |
|-----------|---------------|-------------|-------------------------|
🏃  Starting tunnel for service hello-service.
|-----------|---------------|-------------|------------------------|
| NAMESPACE |     NAME      | TARGET PORT |          URL           |
|-----------|---------------|-------------|------------------------|
| default   | hello-service |             | http://127.0.0.1:58598 |
|-----------|---------------|-------------|------------------------|
🎉  Opening service default/hello-service in default browser...
❗  Because you are using a Docker driver on darwin, the terminal needs to be open to run it.
```

You can find the address for sending requests to the service `http://127.0.0.1:58598`

## Run you tests 
* Add service address to the Endpoint class
* Install java and maven on your computer:  
https://www.oracle.com/java/technologies/javase-downloads.html  
https://maven.apache.org/install.html  

* Run the command to run tests locally:  
```mvn test```
* Check the output: 
```
[INFO] Running com.iceye.HelloServiceTests
Request URI:	http://127.0.0.1:50285/Peter
Request URI:	http://127.0.0.1:50285/PETER
Request URI:	http://127.0.0.1:50285/peter
Request URI:	http://127.0.0.1:50285/peter-641751
Request URI:	http://127.0.0.1:50285/12334
Request URI:	http://127.0.0.1:50285/
Request URI:	http://127.0.0.1:50285/l
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 1.348 s - in com.iceye.HelloServiceTests
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 7, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  12.810 s
[INFO] Finished at: 2020-07-09T02:24:38+03:00
[INFO] ------------------------------------------------------------------------
```
