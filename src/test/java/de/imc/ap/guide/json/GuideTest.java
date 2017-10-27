/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.imc.ap.guide.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Schwantzer
 */
public class GuideTest {
    private Guide testGuide;
    
    public GuideTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testGuide = new Guide("test-guide");
        testGuide.setContentId("de-DE", "content-de");
        testGuide.setContentId("en-GB", "content-en");
        testGuide.addCustomTag("customTag");
        testGuide.addTypedTag("typedTag");
        testGuide.grantAllAccess();
        testGuide.grantUserAccess("user-01");
        testGuide.grantGroupAccess("group-01");
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getId method, of class Guide.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        Guide instance = testGuide;
        String expResult = "test-guide";
        String result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of getContentId method, of class Guide.
     */
    @Test
    public void testGetContentId() {
        System.out.println("getContentId");
        String languageId = "de-DE";
        Guide instance = testGuide;
        String expResult = "content-de";
        String result = instance.getContentId(languageId);
        assertEquals(expResult, result);
    }

    /**
     * Test of getContentIds method, of class Guide.
     */
    @Test
    public void testGetContentIds() {
        System.out.println("getContentIds");
        Guide instance = testGuide;
        Map<String, String> expResult = new LinkedHashMap<>();
        expResult.put("de-DE", "content-de");
        expResult.put("en-GB", "content-en");
        Map<String, String> result = instance.getContentIds();
        assertEquals(expResult, result);
    }

    /**
     * Test of setContentId method, of class Guide.
     */
    @Test
    public void testSetContentId() {
        System.out.println("setContentId");
        String languageId = "en-US";
        String contentId = "content-us";
        Guide instance = testGuide;
        instance.setContentId(languageId, contentId);
        assertEquals(instance.getContentId(languageId), contentId);
    }

    /**
     * Test of removeContentId method, of class Guide.
     */
    @Test
    public void testRemoveContentId() {
        System.out.println("removeContentId");
        String languageId = "en-GB";
        Guide instance = testGuide;
        instance.removeContentId(languageId);
        assertFalse(testGuide.getContentIds().containsKey("en-GB"));
    }

    /**
     * Test of getAllAccessEntries method, of class Guide.
     */
    @Test
    @Ignore
    public void testGetAllAccessEntries() {
        System.out.println("getAllAccessEntries");
        Guide instance = null;
        List<AccessEntry> expResult = null;
        List<AccessEntry> result = instance.getAllAccessEntries();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hasUserAccess method, of class Guide.
     */
    @Test
    public void testHasUserAccess() {
        System.out.println("hasUserAccess");
        assertTrue(testGuide.hasUserAccess("user-01"));
        assertFalse(testGuide.hasUserAccess("user-02"));
    }

    /**
     * Test of hasGroupAccess method, of class Guide.
     */
    @Test
    public void testHasGroupAccess() {
        System.out.println("hasGroupAccess");
        assertTrue(testGuide.hasGroupAccess("group-01"));
        assertFalse(testGuide.hasGroupAccess("group-02"));
    }

    /**
     * Test of hasAllAccess method, of class Guide.
     */
    @Test
    public void testHasAllAccess() {
        System.out.println("hasAllAccess");
        assertTrue(testGuide.hasAllAccess());
        testGuide.revokeAllAccess();
        assertFalse(testGuide.hasAllAccess());
    }

    /**
     * Test of grantUserAccess method, of class Guide.
     */
    @Test
    public void testGrantUserAccess() {
        System.out.println("grantUserAccess");
        testGuide.grantUserAccess("user-02");
        assertTrue(testGuide.hasUserAccess("user-02"));
    }

    /**
     * Test of grantGroupAccess method, of class Guide.
     */
    @Test
    public void testGrantGroupAccess() {
        System.out.println("grantGroupAccess");
        testGuide.grantGroupAccess("group-02");
        assertTrue(testGuide.hasGroupAccess("group-02"));
    }

    /**
     * Test of grantAllAccess method, of class Guide.
     */
    @Test
    public void testGrantAllAccess() {
        System.out.println("grantAllAccess");
        testGuide.revokeAllAccess();
        assertFalse(testGuide.hasAllAccess());
        testGuide.grantAllAccess();
        assertTrue(testGuide.hasAllAccess());
    }

    /**
     * Test of revokeUserAccess method, of class Guide.
     */
    @Test
    public void testRevokeUserAccess() {
        System.out.println("revokeUserAccess");
        String userId = "user-01";
        testGuide.revokeUserAccess(userId);
        assertFalse(testGuide.hasUserAccess(userId));
    }

    /**
     * Test of revokeGroupAccess method, of class Guide.
     */
    @Test
    public void testRevokeGroupAccess() {
        System.out.println("revokeGroupAccess");
        String groupId = "group-01";
        testGuide.revokeGroupAccess(groupId);
        assertFalse(testGuide.hasGroupAccess(groupId));
    }

    /**
     * Test of revokeAllAccess method, of class Guide.
     */
    @Test
    public void testRevokeAllAccess() {
        System.out.println("revokeAllAccess");
        testGuide.revokeAllAccess();
        assertFalse(testGuide.hasAllAccess());
    }

    /**
     * Test of getAllTags method, of class Guide.
     */
    @Test
    @Ignore
    public void testGetAllTags() {
        System.out.println("getAllTags");
        Guide instance = null;
        List<Tag> expResult = null;
        List<Tag> result = instance.getAllTags();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCustomTags method, of class Guide.
     */
    @Test
    public void testGetCustomTags() {
        System.out.println("getCustomTags");
        String[] expResult = {"customTag"};
        assertArrayEquals(expResult, testGuide.getCustomTags().toArray());
    }

    /**
     * Test of getTypedTags method, of class Guide.
     */
    @Test
    public void testGetTypedTags() {
        System.out.println("getTypedTags");
        String[] expResult = {"typedTag"};
        assertArrayEquals(expResult, testGuide.getTypedTags().toArray());
    }

    /**
     * Test of addCustomTag method, of class Guide.
     */
    @Test
    public void testAddCustomTag() {
        System.out.println("addCustomTag");
        String tag = "newTag";
        testGuide.addCustomTag(tag);
        assertTrue(testGuide.getCustomTags().contains(tag));
    }

    /**
     * Test of removeCustomTag method, of class Guide.
     */
    @Test
    public void testRemoveCustomTag() {
        System.out.println("removeCustomTag");
        String value = "customTag";
        testGuide.removeCustomTag(value);
        assertFalse(testGuide.getCustomTags().contains(value));
    }

    /**
     * Test of addTypedTag method, of class Guide.
     */
    @Test
    public void testAddTypedTag() {
        System.out.println("addTypedTag");
        String value = "newTag";
        testGuide.addTypedTag(value);
        assertTrue(testGuide.getTypedTags().contains(value));
    }

    /**
     * Test of removeTypedTag method, of class Guide.
     */
    @Test
    public void testRemoveTypedTag() {
        System.out.println("removeTypedTag");
        String value = "typedTag";
        testGuide.removeTypedTag(value);
        assertFalse(testGuide.getTypedTags().contains(value));
    }

    /**
     * Test of getSteps method, of class Guide.
     */
    @Test
    @Ignore
    public void testGetSteps() {
        System.out.println("getSteps");
        Guide instance = null;
        List<Step> expResult = null;
        List<Step> result = instance.getSteps();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getStep method, of class Guide.
     */
    @Test
    @Ignore
    public void testGetStep() {
        System.out.println("getStep");
        String stepId = "";
        Guide instance = null;
        Step expResult = null;
        Step result = instance.getStep(stepId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of indexOfStep method, of class Guide.
     */
    @Test
    @Ignore
    public void testIndexOfStep() {
        System.out.println("indexOfStep");
        String stepId = "";
        Guide instance = null;
        int expResult = 0;
        int result = instance.indexOfStep(stepId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addStep method, of class Guide.
     */
    @Test
    @Ignore
    public void testAddStep_int_Step() {
        System.out.println("addStep");
        int index = 0;
        Step step = null;
        Guide instance = null;
        instance.addStep(index, step);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addStep method, of class Guide.
     */
    @Test
    @Ignore
    public void testAddStep_Step() {
        System.out.println("addStep");
        Step step = null;
        Guide instance = null;
        instance.addStep(step);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeStep method, of class Guide.
     */
    @Test
    @Ignore
    public void testRemoveStep() {
        System.out.println("removeStep");
        String stepId = "";
        Guide instance = null;
        de.imc.ap.guide.json.Step expResult = null;
        de.imc.ap.guide.json.Step result = instance.removeStep(stepId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of asJson method, of class Guide.
     */
    @Test
    @Ignore
    public void testAsJson() {
        System.out.println("asJson");
        Guide instance = null;
        ObjectNode expResult = null;
        ObjectNode result = instance.asJson();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
