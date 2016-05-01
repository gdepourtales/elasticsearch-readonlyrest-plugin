package org.elasticsearch.plugin.readonlyrest.acl.blocks;

import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugin.readonlyrest.acl.RequestContext;
import org.elasticsearch.plugin.readonlyrest.acl.blocks.rules.RuleNotConfiguredException;

import java.io.IOException;


class IndexMapping {

    private String source;
    private String target;
    private ESLogger logger;


    IndexMapping(Settings s, ESLogger esLogger) throws RuleNotConfiguredException {
        if (s == null || s.get("source") == null || s.get("target") == null) {
            throw new RuleNotConfiguredException();
        }
        source = s.get("source");
        target = s.get("target");
        logger = esLogger;
    }

    RequestContext apply(RequestContext rc) throws IOException {
        for (int i = 0; i < rc.getIndices().length; i++) {
            if (rc.getIndices()[i].equals(source)) {
                rc.getIndices()[i] = target;
            }
        }

        logger.info("Replacing index '" + source + "' by '" + target + "'");
        rc.setContent(rc.getContent().replace("\"" + source + "\"", "\"" + target + "\""));


        return rc;
    }

}
