FROM eclipse-temurin:17-jdk

RUN mkdir -p /opt/derby && \
    curl -o /tmp/derby.tar.gz https://archive.apache.org/dist/db/derby/db-derby-10.16.1.1/db-derby-10.16.1.1-bin.tar.gz && \
    tar -xzf /tmp/derby.tar.gz -C /opt/derby --strip-components=1 && \
    rm /tmp/derby.tar.gz

# Set environment variables
ENV DERBY_HOME=/opt/derby
ENV PATH=$PATH:$DERBY_HOME/bin

# Create directory for databases with proper permissions
RUN mkdir -p /dbs && chmod a+rwx /dbs

# Expose Derby default port
EXPOSE 1527

# Start Derby in network server mode
CMD ["/opt/derby/bin/startNetworkServer", "-h", "0.0.0.0"]