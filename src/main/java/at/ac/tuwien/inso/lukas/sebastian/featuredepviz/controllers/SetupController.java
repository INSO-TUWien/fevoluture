package at.ac.tuwien.inso.lukas.sebastian.featuredepviz.controllers;

import at.ac.tuwien.inso.lukas.sebastian.featuredepviz.indexers.Indexer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin
public class SetupController extends APIController {

    @Autowired
    Indexer indexer;

    @RequestMapping("/api/setup")
    public void setup() throws IOException {
        indexer.index();
    }

    @RequestMapping("/api/clear")
    public void clear() throws IOException {
        indexer.clearAll();
    }


}
