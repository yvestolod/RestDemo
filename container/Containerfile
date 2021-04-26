FROM ibmcom/websphere-liberty:kernel-java8-ibmjava-ubi
COPY server.xml  /config
COPY RestDemo.war /config/dropins/claims.war
RUN /liberty/wlp/bin/installUtility install --acceptLicense /config/server.xml
