package io.github.codesaiyan.wordsearchapi.controllers;

import io.github.codesaiyan.wordsearchapi.services.WordSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Pranav Rao
 */
@RestController("/")
public class WordSearchController {

    @Autowired
    WordSearchService wordSearchService;

    @GetMapping("/wordgrid")
    public String createWordGrid(@RequestParam int gridSize, @RequestParam List<String> words) {
        char[][] grid = wordSearchService.generateGrid(gridSize, words);
        StringBuilder gridToString = new StringBuilder();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                gridToString.append(grid[i][j]).append(" ");
            }
            gridToString.append("\r\n");
        }
        return gridToString.toString();
    }
}
