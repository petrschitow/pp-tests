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