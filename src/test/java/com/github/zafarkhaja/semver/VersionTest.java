/*
 * The MIT License
 *
 * Copyright 2012 Zafar Khaja <zafarkhaja@gmail.com>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.zafarkhaja.semver;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

/**
 *
 * @author Zafar Khaja <zafarkhaja@gmail.com>
 */
@RunWith(Enclosed.class)
public class VersionTest {

    public static class CoreFunctionalityTest {

        @Test
        public void mayHavePreReleaseFollowingPatchAppendedWithHyphen() {
            Version v = Version.valueOf("1.2.3-alpha");
            assertEquals("alpha", v.getPreReleaseVersion());
        }

        @Test
        public void preReleaseShouldHaveLowerPrecedenceThanAssociatedNormal() {
            Version v1 = Version.valueOf("1.3.7");
            Version v2 = Version.valueOf("1.3.7-alpha");
            assertTrue(0 < v1.compareTo(v2));
            assertTrue(0 > v2.compareTo(v1));
        }

        @Test
        public void mayHaveBuildFollowingPatchOrPreReleaseAppendedWithPlus() {
            Version v = Version.valueOf("1.2.3+build");
            assertEquals("build", v.getBuildMetadata());
        }

        @Test
        public void shouldIgnoreBuildMetadataWhenDeterminingVersionPrecedence() {
            Version v1 = Version.valueOf("1.3.7-beta");
            Version v2 = Version.valueOf("1.3.7-beta+build.1");
            Version v3 = Version.valueOf("1.3.7-beta+build.2");
            assertTrue(0 == v1.compareTo(v2));
            assertTrue(0 == v1.compareTo(v3));
            assertTrue(0 == v2.compareTo(v3));
        }

        @Test
        public void shouldHaveGreaterThanMethodReturningBoolean() {
            Version v1 = Version.valueOf("2.3.7");
            Version v2 = Version.valueOf("1.3.7");
            assertTrue(v1.greaterThan(v2));
            assertFalse(v2.greaterThan(v1));
            assertFalse(v1.greaterThan(v1));
        }

        @Test
        public void shouldHaveGreaterThanOrEqualsToMethodReturningBoolean() {
            Version v1 = Version.valueOf("2.3.7");
            Version v2 = Version.valueOf("1.3.7");
            assertTrue(v1.greaterThanOrEqualsTo(v2));
            assertFalse(v2.greaterThanOrEqualsTo(v1));
            assertTrue(v1.greaterThanOrEqualsTo(v1));
        }

        @Test
        public void shouldHaveLessThanMethodReturningBoolean() {
            Version v1 = Version.valueOf("2.3.7");
            Version v2 = Version.valueOf("1.3.7");
            assertFalse(v1.lessThan(v2));
            assertTrue(v2.lessThan(v1));
            assertFalse(v1.lessThan(v1));
        }

        @Test
        public void shouldHaveLessThanOrEqualsToMethodReturningBoolean() {
            Version v1 = Version.valueOf("2.3.7");
            Version v2 = Version.valueOf("1.3.7");
            assertFalse(v1.lessThanOrEqualsTo(v2));
            assertTrue(v2.lessThanOrEqualsTo(v1));
            assertTrue(v1.lessThanOrEqualsTo(v1));
        }

        @Test
        public void shouldOverrideEqualsMethod() {
            Version v1 = Version.valueOf("2.3.7");
            Version v2 = Version.valueOf("2.3.7");
            Version v3 = Version.valueOf("1.3.7");
            assertTrue(v1.equals(v1));
            assertTrue(v1.equals(v2));
            assertFalse(v1.equals(v3));
        }

        @Test
        public void shouldCorrectlyCompareAllVersionsFromSpecification() {
            String[] versions = {
                "1.0.0-alpha",
                "1.0.0-alpha.1",
                "1.0.0-beta.2",
                "1.0.0-beta.11",
                "1.0.0-rc.1",
                "1.0.0",
            };
            for (int i = 1; i < versions.length; i++) {
                Version v1 = Version.valueOf(versions[i-1]);
                Version v2 = Version.valueOf(versions[i]);
                assertTrue(v1.lessThan(v2));
            }
        }

        @Test
        public void shouldHaveStaticFactoryMethod() {
            Version v = Version.valueOf("1.0.0-rc.1+build.1");
            assertEquals(1, v.getMajorVersion());
            assertEquals(0, v.getMinorVersion());
            assertEquals(0, v.getPatchVersion());
            assertEquals("1.0.0", v.getNormalVersion());
            assertEquals("rc.1", v.getPreReleaseVersion());
            assertEquals("build.1", v.getBuildMetadata());
        }

        @Test
        public void shouldProvideIncrementMajorVersionMethod() {
            Version v = Version.valueOf("1.2.3");
            Version incrementedMajor = v.incrementMajorVersion();
            assertEquals("2.0.0", incrementedMajor.toString());
        }

        @Test
        public void shouldIncrementMajorVersionWithPreReleaseIfProvided() {
            Version v = Version.valueOf("1.2.3");
            Version incrementedMajor = v.incrementMajorVersion("beta");
            assertEquals("2.0.0-beta", incrementedMajor.toString());
        }

        @Test
        public void shouldProvideIncrementMinorVersionMethod() {
            Version v = Version.valueOf("1.2.3");
            Version incrementedMinor = v.incrementMinorVersion();
            assertEquals("1.3.0", incrementedMinor.toString());
        }

        @Test
        public void shouldIncrementMinorVersionWithPreReleaseIfProvided() {
            Version v = Version.valueOf("1.2.3");
            Version incrementedMinor = v.incrementMinorVersion("alpha");
            assertEquals("1.3.0-alpha", incrementedMinor.toString());
        }

        @Test
        public void shouldProvideIncrementPatchVersionMethod() {
            Version v = Version.valueOf("1.2.3");
            Version incrementedPatch = v.incrementPatchVersion();
            assertEquals("1.2.4", incrementedPatch.toString());
        }

        @Test
        public void shouldIncrementPatchVersionWithPreReleaseIfProvided() {
            Version v = Version.valueOf("1.2.3");
            Version incrementedPatch = v.incrementPatchVersion("rc");
            assertEquals("1.2.4-rc", incrementedPatch.toString());
        }

        @Test
        public void shouldDropBuildMetadataWhenIncrementing() {
            Version v = Version.valueOf("1.2.3-alpha+build");

            Version major1 = v.incrementMajorVersion();
            assertEquals("2.0.0", major1.toString());
            Version major2 = v.incrementMajorVersion("beta");
            assertEquals("2.0.0-beta", major2.toString());

            Version minor1 = v.incrementMinorVersion();
            assertEquals("1.3.0", minor1.toString());
            Version minor2 = v.incrementMinorVersion("beta");
            assertEquals("1.3.0-beta", minor2.toString());

            Version patch1 = v.incrementPatchVersion();
            assertEquals("1.2.4", patch1.toString());
            Version patch2 = v.incrementPatchVersion("beta");
            assertEquals("1.2.4-beta", patch2.toString());
        }

        @Test
        public void shouldProvideSetPreReleaseVersionMethod() {
            Version v1 = Version.valueOf("1.0.0");
            Version v2 = v1.setPreReleaseVersion("alpha");
            assertEquals("1.0.0-alpha", v2.toString());
        }

        @Test
        public void shouldDropBuildMetadataWhenSettingPreReleaseVersion() {
            Version v1 = Version.valueOf("1.0.0-alpha+build");
            Version v2 = v1.setPreReleaseVersion("beta");
            assertEquals("1.0.0-beta", v2.toString());
        }

        @Test
        public void shouldProvideSetBuildMetadataMethod() {
            Version v1 = Version.valueOf("1.0.0");
            Version v2 = v1.setBuildMetadata("build");
            assertEquals("1.0.0+build", v2.toString());
        }

        @Test
        public void shouldBeImmutable() {
            Version version = Version.valueOf("1.2.3");

            Version incementedMajor = version.incrementMajorVersion();
            assertNotSame(version, incementedMajor);

            Version incementedMinor = version.incrementMinorVersion();
            assertNotSame(version, incementedMinor);

            Version incementedPatch = version.incrementPatchVersion();
            assertNotSame(version, incementedPatch);

            Version preReleaseSet = version.setPreReleaseVersion("alpha");
            assertNotSame(version, preReleaseSet);

            Version buildSet = version.setBuildMetadata("build");
            assertNotSame(version, buildSet);
        }
    }

    public static class EqualsMethodTest {

        @Test
        public void shouldBeReflexive() {
            Version v1 = Version.valueOf("2.3.7");
            assertTrue(v1.equals(v1));
        }

        @Test
        public void shouldBeSymmetric() {
            Version v1 = Version.valueOf("2.3.7");
            Version v2 = Version.valueOf("2.3.7");
            assertTrue(v1.equals(v2));
            assertTrue(v2.equals(v1));
        }

        @Test
        public void shouldBeTransitive() {
            Version v1 = Version.valueOf("2.3.7");
            Version v2 = Version.valueOf("2.3.7");
            Version v3 = Version.valueOf("2.3.7");
            assertTrue(v1.equals(v2));
            assertTrue(v2.equals(v3));
            assertTrue(v1.equals(v3));
        }

        @Test
        public void shouldBeConsistent() {
            Version v1 = Version.valueOf("2.3.7");
            Version v2 = Version.valueOf("2.3.7");
            assertTrue(v1.equals(v2));
            assertTrue(v1.equals(v2));
            assertTrue(v1.equals(v2));
        }

        @Test
        public void shouldReturnFalseIfOtherVersionIsOfDifferentType() {
            Version v1 = Version.valueOf("2.3.7");
            assertFalse(v1.equals(new String("2.3.7")));
        }

        @Test
        public void shouldReturnFalseIfOtherVersionIsNull() {
            Version v1 = Version.valueOf("2.3.7");
            Version v2 = null;
            assertFalse(v1.equals(v2));
        }
    }

    public static class HashCodeMethodTest {

        @Test
        public void shouldReturnSameHashCodeIfVersionsAreEqual() {
            Version v1 = Version.valueOf("2.3.7");
            Version v2 = Version.valueOf("2.3.7");
            assertTrue(v1.equals(v2));
            assertEquals(v1.hashCode(), v2.hashCode());
        }
    }

    public static class ToStringMethodTest {

        @Test
        public void shouldReturnStringRepresentation() {
            String value = "1.2.3-beta+build";
            Version v = Version.valueOf(value);
            assertEquals(value, v.toString());
        }
    }
}
