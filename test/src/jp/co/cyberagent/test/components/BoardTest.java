package jp.co.cyberagent.test.components;

import jp.co.cyberagent.components.Board;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void testCreateBoard() {
        try {
            new Board(5, 5, 5);
        } catch (Exception e) {
            fail();
        }
    }

//    public void testGetSize() {
//
//    }
//
//    public void testToggleMineCheckSquare() {
//
//    }
//
//    public void testOpenSquare() {
//
//    }
//
//    public void testGetSquare() {
//
//    }
}