import rest = require("rest");
import defaultRequest = require("rest/interceptor/defaultRequest");
import errorCode = require("rest/interceptor/errorCode");
import mime = require("rest/interceptor/mime");
import registry = require("rest/mime/registry");
import hal = require("rest/mime/type/application/hal");

registry.register("application/hal+json", hal);

export default rest
    .wrap(mime, { registry })
    .wrap(errorCode)
    .wrap(defaultRequest, { headers: { Accept: "application/hal+json" } });
