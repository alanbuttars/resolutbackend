package com.alanbuttars.resolut.backend.models;

import org.junit.Assert;
import org.junit.Test;

public class ResultTest {
    @Test
    public void testSuccess() {
        Result<Integer> result = Result.success(1);
        Assert.assertTrue(result.succeeded());
        Assert.assertEquals(Integer.valueOf(1), result.getTarget());
        Assert.assertNull(result.getErrorMessage());
    }

    @Test
    public void testFailure() {
        Result<Integer> result = Result.failure("blah");
        Assert.assertFalse(result.succeeded());
        Assert.assertNull(result.getTarget());
        Assert.assertEquals("blah", result.getErrorMessage());
    }
}
