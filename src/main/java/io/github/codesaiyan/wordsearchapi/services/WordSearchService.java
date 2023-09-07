package io.github.codesaiyan.wordsearchapi.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Pranav Rao
 */
@Service
public class WordSearchService {

    private record Coordinate(int x, int y) {
    }

    private enum Direction {
        HORIZONTAL(0, 1),
        VERTICAL(1, 0),
        DIAGONAL(1, 1),
        HORIZONTAL_INVERSE(0, -1),
        VERTICAL_INVERSE(-1, 0),
        DIAGONAL_INVERSE(-1, -1);

        private final int xDelta;
        private final int yDelta;

        Direction(int xDelta, int yDelta) {
            this.xDelta = xDelta;
            this.yDelta = yDelta;
        }

        public int getXDelta() {
            return xDelta;
        }

        public int getYDelta() {
            return yDelta;
        }
    }

    public char[][] generateGrid(int gridSize, List<String> words) {
        List<Coordinate> coordinates = generateCoordinates(gridSize);
        char[][] contents = new char[gridSize][gridSize];
        fillGridWithUnderscores(contents);

        for (String word : words) {
            Collections.shuffle(coordinates);
            for (Coordinate coordinate : coordinates) {
                Direction selectedDirection = getDirectionForFit(contents, word, coordinate);
                if (selectedDirection != null) {
                    placeWordInSelectedDirection(contents, word, coordinate, selectedDirection);
                    break;
                }
            }
        }
        randomFillGrid(contents);
        return contents;
    }

    public void displayGrid(char[][] contents) {
        for (char[] row : contents) {
            System.out.println(new String(row));
        }
    }

    private List<Coordinate> generateCoordinates(int gridSize) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                coordinates.add(new Coordinate(i, j));
            }
        }
        return coordinates;
    }

    private void fillGridWithUnderscores(char[][] contents) {
        for (char[] row : contents) {
            Arrays.fill(row, '_');
        }
    }

    private void randomFillGrid(char[][] contents) {
        for (int i = 0; i < contents.length; i++) {
            for (int j = 0; j < contents[i].length; j++) {
                if (contents[i][j] == '_') {
                    contents[i][j] = getRandomUppercaseLetter();
                }
            }
        }
    }

    private Direction getDirectionForFit(char[][] contents, String word, Coordinate coordinate) {
        List<Direction> directions = Arrays.asList(Direction.values());
        Collections.shuffle(directions);
        return directions.stream()
                .filter(dir -> doesFit(contents, word, coordinate, dir))
                .findFirst()
                .orElse(null);
    }

    private boolean doesFit(char[][] contents, String word, Coordinate coordinate, Direction direction) {
        int gridSize = contents.length;
        int x = coordinate.x;
        int y = coordinate.y;

        for (char ch : word.toCharArray()) {
            if (x < 0 || x >= gridSize || y < 0 || y >= gridSize || (contents[x][y] != '_' && contents[x][y] != ch)) {
                return false;
            }
            x += direction.getXDelta();
            y += direction.getYDelta();
        }
        return true;
    }

    private void placeWordInSelectedDirection(char[][] contents, String word, Coordinate coordinate, Direction direction) {
        int x = coordinate.x;
        int y = coordinate.y;

        for (char c : word.toCharArray()) {
            contents[x][y] = c;
            x += direction.getXDelta();
            y += direction.getYDelta();
        }
    }

    private char getRandomUppercaseLetter() {
        return (char) ThreadLocalRandom.current().nextInt('A', 'Z' + 1);
    }
}
