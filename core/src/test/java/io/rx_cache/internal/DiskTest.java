/*
 * Copyright 2015 Victor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.rx_cache.internal;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import io.rx_cache.internal.common.BaseTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;

public class DiskTest extends BaseTest {
    private final static String KEY = "store";
    private final static String VALUE = "dummy";

    @Test public void When_A_Record_Is_Supplied_Retrieve_It() {
        disk.save(KEY, new Record(new Mock(VALUE)), false, null);

        Record<Mock> diskRecord = disk.retrieveRecord(KEY, false, null);
        assertThat(diskRecord.getData().getMessage(), is(VALUE));
    }

    @Test public void When_A_Record_Collection_Is_Supplied_Retrieve_It() {
        List<Mock> mocks = Arrays.asList(new Mock(VALUE), new Mock(VALUE + 1));
        disk.save(KEY, new Record(mocks), false, null);

        Record<List<Mock>> diskRecord = disk.retrieveRecord(KEY, false, null);
        assertThat(diskRecord.getData().get(0).getMessage(), is(VALUE));
        assertThat(diskRecord.getData().get(1).getMessage(), is(VALUE+1));
    }

    @Test public void When_A_Record_Array_Is_Supplied_Retrieve_It() {
        Mock[] mocks = {new Mock(VALUE), new Mock(VALUE+1)};
        disk.save(KEY, new Record(mocks), false, null);

        Record<Mock[]> diskRecord = disk.retrieveRecord(KEY, false, null);
        assertThat(diskRecord.getData()[0].getMessage(), is(VALUE));
        assertThat(diskRecord.getData()[1].getMessage(), is(VALUE+1));
    }

    @Test public void When_A_Record_Map_Is_Supplied_Retrieve_It() {
        Map<Integer, Mock> mocks = new HashMap();
        mocks.put(1, new Mock(VALUE));
        mocks.put(2, new Mock(VALUE + 1));

        disk.save(KEY, new Record(mocks), false, null);

        Record<Map<Integer, Mock>> diskRecord = disk.retrieveRecord(KEY, false, null);
        assertThat(diskRecord.getData().get(1).getMessage(), is(VALUE));
        assertThat(diskRecord.getData().get(2).getMessage(), is(VALUE+1));
    }

    @Test public void When_A_Collection_Is_Supplied_Retrieve_It() {
        List<Mock> mockArrayList = new ArrayList();
        mockArrayList.add(new Mock(VALUE));
        mockArrayList.add(new Mock(VALUE + 1));

        disk.save(KEY, mockArrayList, false, null);
        mockArrayList = disk.retrieveCollection(KEY, ArrayList.class, Mock.class);

        assertThat(mockArrayList.get(0).getMessage(), is(VALUE));
        assertThat(mockArrayList.get(1).getMessage(), is(VALUE + 1));

        Vector<Mock> mocksVector = new Vector(3, 2);
        mocksVector.add(new Mock(VALUE));
        mocksVector.add(new Mock(VALUE + 1));

        disk.save(KEY, mocksVector, false, null);
        mocksVector = disk.retrieveCollection(KEY, Vector.class, Mock.class);

        assertThat(mocksVector.get(0).getMessage(), is(VALUE));
        assertThat(mocksVector.get(1).getMessage(), is(VALUE+1));
    }

    @Test public void When_A_Map_Is_Supplied_Retrieve_It() {
        Map<Integer, Mock> mocksHashMap = new HashMap();
        mocksHashMap.put(1, new Mock(VALUE));
        mocksHashMap.put(2, new Mock(VALUE + 1));

        disk.save(KEY, mocksHashMap, false, null);

        mocksHashMap = disk.retrieveMap(KEY, TreeMap.class, Integer.class, Mock.class);
        assertThat(mocksHashMap.get(1).getMessage(), is(VALUE));
        assertThat(mocksHashMap.get(2).getMessage(), is(VALUE+1));
    }

    @Test public void When_An_Array_Is_Supplied_Retrieve_It() {
        Mock[] mocksArray = {new Mock(VALUE), new Mock(VALUE+1)};
        disk.save(KEY, mocksArray, false, null);

        mocksArray = disk.retrieveArray(KEY, Mock.class);
        assertThat(mocksArray[0].getMessage(), is(VALUE));
        assertThat(mocksArray[1].getMessage(), is(VALUE+1));
    }

    @Test public void When_Encrypt_Is_False_Then_Retrieve_Record_Without_Encrypt() {
        disk.save(KEY, new Record(new Mock(VALUE)), false, null);
        Record<Mock> diskRecord = disk.retrieveRecord(KEY, false, null);
        assertThat(diskRecord.getData().getMessage(), is(VALUE));
    }

    @Test public void When_Encrypt_Is_True_Then_Retrieve_Record_Decrypted() {
        disk.save(KEY, new Record(new Mock(VALUE)), true, "key");
        Record<Mock> diskRecord = disk.retrieveRecord(KEY, true, "key");
        assertThat(diskRecord.getData().getMessage(), is(VALUE));
    }

    @Test public void When_Encrypt_Is_False_And_I_Try_Retrieve_It_Encrypted_Then_Record_Is_Null() {
        disk.save(KEY, new Record(new Mock(VALUE)), false, null);
        Record<Mock> diskRecord = disk.retrieveRecord(KEY, true, "key");
        assertNull(diskRecord);
    }

    @Test public void When_Encrypt_Is_True_And_I_Try_Retrieve_It_Without_Encrypt_Then_Record_Is_Null() {
        disk.save(KEY, new Record(new Mock(VALUE)), true, "key");
        Record<Mock> diskRecord = disk.retrieveRecord(KEY, false, null);
        assertNull(diskRecord);
    }

    @Test public void When_Encrypt_Is_True_And_I_Try_Retrieve_It_With_Another_Key_Then_Record_Is_Null() {
        disk.save(KEY, new Record(new Mock(VALUE)), true, "key");
        Record<Mock> diskRecord = disk.retrieveRecord(KEY, true, "otherkey");
        assertNull(diskRecord);
    }
}
