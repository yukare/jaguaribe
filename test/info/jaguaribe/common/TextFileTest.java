/*
 * Copyright (c) 2012 by Fernando Correa da Conceição
 *
 * This is released under MIT license, read the file LICENSE.txt or
 * http://opensource.org/licenses/mit-license.php .
 */
package info.jaguaribe.common;

import br.net.jaguaribe.common.TextFile;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Fernando Correa da Conceição.
 */
public class TextFileTest {
    public TextFileTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of write method, of class TextFile.
     *
     */
    @Test
    public void testWrite() throws IOException {
        System.out.println("write");
        String text = "qwertyuiop";
        TextFile instance = new TextFile("test.txt");
        instance.write(text);

        String content = instance.read();
        assertEquals(text, content);
    }

    /**
     * Test of read method, of class TextFile.
     *
     * @todo Change the test to read a existing file in disk.
     */
    @Test
    public void testRead() throws IOException {
        System.out.println("write");
        String text = "qwertyuiop";
        TextFile instance = new TextFile("test.txt");
        instance.write(text);
        String content = instance.read();
        assertEquals(text, content);
    }

    /**
     * Test of getFilename method, of class TextFile.
     */
    @Test
    public void testGetFilename() {
        System.out.println("getFilename");
        TextFile instance = new TextFile("teste.txt");
        String expResult = "teste.txt";
        String result = instance.getFilename();
        assertEquals(expResult, result);
    }

    /**
     * Test of setFilename method, of class TextFile.
     */
    @Test
    public void testSetFilename() {
        System.out.println("setFilename");
        String filename = "teste.txt";
        TextFile instance = new TextFile("");
        instance.setFilename(filename);
        String result = instance.getFilename();
        assertEquals(filename, result);
    }

    /**
     * Test of getCharset method, of class TextFile.
     */
    @Test
    public void testGetCharset() {
        System.out.println("getCharset");
        TextFile instance = new TextFile("");
        Charset result = instance.getCharset();
        Charset expected = Charset.forName("UTF-8");
        assertEquals(expected, result);
    }

    /**
     * Test of setCharset method, of class TextFile.
     */
    @Test
    public void testSetCharset() {
        System.out.println("setCharset");
        Charset charset = Charset.forName("UTF-8");
        TextFile instance = new TextFile("");
        instance.setCharset(charset);
        assertEquals(charset, instance.getCharset());
    }
}
