/*
 * Copyright (c) 2012, dooApp <contact@dooapp.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of dooApp nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.dooapp.fxform.filter;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: Antoine Mischler <antoine@dooapp.com>
 * Date: 12/09/11
 * Time: 16:45
 */
public class ReorderFilterTest extends AbstractFilterTest {

    @Override
    FieldFilter createFilter() {
        return new ReorderFilter(new String[]{"doubleProperty", "booleanProperty"});
    }

    @Test
    public void testFilter() throws Exception {
        Assert.assertEquals("doubleProperty", filtered.get(0).getName());
        Assert.assertEquals("booleanProperty", filtered.get(1).getName());
        Assert.assertEquals("stringProperty", filtered.get(2).getName());
        Assert.assertEquals("integerProperty", filtered.get(3).getName());
        Assert.assertEquals("objectProperty", filtered.get(4).getName());
    }
}
