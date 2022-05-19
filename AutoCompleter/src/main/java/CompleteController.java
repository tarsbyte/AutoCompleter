
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompleteController {

    private final AtomicLong counter = new AtomicLong();
    AutoCompleter autoCompleter;

    {
        try {
            autoCompleter = new AutoCompleter();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    @GetMapping("/complete")
    public List complete(@RequestParam(value = "name") String name) throws IOException {
        LinkedList list = autoCompleter.giveCitiesWithPrefix(name);
        return list;
    }
}