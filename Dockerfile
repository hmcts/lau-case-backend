ARG APP_INSIGHTS_AGENT_VERSION=3.6.2

# Application image

FROM hmctspublic.azurecr.io/base/java:21-distroless

COPY lib/applicationinsights.json /opt/app/
COPY build/libs/lau-case-backend.jar /opt/app/

EXPOSE 4550
CMD [ "lau-case-backend.jar" ]
