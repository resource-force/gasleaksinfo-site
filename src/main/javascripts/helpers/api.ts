import rest from "rest";
import defaultRequest from "rest/interceptor/defaultRequest";
import errorCode from "rest/interceptor/errorCode";
import mime from "rest/interceptor/mime";
import registry from "rest/mime/registry";

export default rest
    .wrap(mime, { registry })
    .wrap(errorCode)
    .wrap(defaultRequest, { headers: { Accept: "application/hal+json" } });
