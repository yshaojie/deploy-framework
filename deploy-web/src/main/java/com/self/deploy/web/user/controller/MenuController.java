package com.self.deploy.web.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shaojieyue
 * Created time 2017-02-12 14:44
 */


@RestController
@RequestMapping(value = "/menu", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @RequestMapping(value = "list",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String list(){
        return "[\n" +
                "  {\n" +
                "    \"text\": \"Menu Heading\",\n" +
                "    \"heading\": \"true\",\n" +
                "    \"translate\": \"sidebar.heading.HEADER\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"text\": \"服务列表\",\n" +
                "    \"sref\": \"app.server_list\",\n" +
                "    \"icon\": \"fa fa-file-o\",\n" +
                "    \"translate\": \"sidebar.nav.SINGLEVIEW\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"text\": \"Menu\",\n" +
                "    \"sref\": \"#\",\n" +
                "    \"icon\": \"icon-folder\",\n" +
                "    \"submenu\": [\n" +
                "      { \"text\": \"Sub Menu\", \n" +
                "        \"sref\": \"app.submenu\", \n" +
                "        \"translate\": \"sidebar.nav.menu.SUBMENU\" \n" +
                "      }    \n" +
                "    ],\n" +
                "    \"translate\": \"sidebar.nav.menu.MENU\"\n" +
                "  }\n" +
                "]\n";
    }
}
