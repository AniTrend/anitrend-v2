/*
 * Copyright (C) 2020  AniTrend
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package co.anitrend.data.review.model.mutation;

import static org.junit.Assert.*;

class SaveReviewMutationTest {

    @Test
    fun `given a summary that is greater than the minimum requirement and less than the maximum`() {
        val input = SaveReviewMutation(
            mediaId = 0,
            body = "This is a sample body which should not pass",
            score = 50,
            private = false,
            summary = "Short summary which should pass the test"
        )

        assertTrue(input.summary.length > 20)

        val expected = input.isSummaryValid()
        assertTrue(expected)
    }

    @Test
    fun `given a summary that is equal to the minimum requirement`() {
        val input = SaveReviewMutation(
            mediaId = 0,
            body = "Lorem ipsum dolor sit amet",
            score = 50,
            private = false,
            summary = "Lorem ipsum dolor .."
        )
        assertEquals(20, input.summary.length)

        val expected = input.isSummaryValid()
        assertTrue(expected)
    }

    @Test
    fun `given a summary that is one character less than the minimum requirement`() {
        val input = SaveReviewMutation(
            mediaId = 0,
            body = "Lorem ipsum dolor sit amet",
            score = 50,
            private = false,
            summary = "Lorem ipsum dolor ."
        )

        assertEquals(19, input.summary.length)

        val expected = input.isSummaryValid()
        assertFalse(expected)
    }

    @Test
    fun `given a summary that is one character more than the minimum requirement`() {
        val input = SaveReviewMutation(
            mediaId = 0,
            body = "Lorem ipsum dolor sit amet",
            score = 50,
            private = false,
            summary = "Lorem ipsum dolor sit"
        )

        assertEquals(21, input.summary.length)

        val expected = input.isSummaryValid()
        assertTrue(expected)
    }

    @Test
    fun `given a summary that is equal to the maximum requirement`() {
        val input = SaveReviewMutation(
            mediaId = 0,
            body = "Lorem ipsum dolor sit amet",
            score = 50,
            private = false,
            summary = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam sollicitudin condimentum nisi eu tempor. Proin dictum in me"
        )

        assertEquals(120, input.summary.length)

        val expected = input.isSummaryValid()
        assertTrue(expected)
    }

    @Test
    fun `given a summary that is one character more than the maximum requirement`() {
        val input = SaveReviewMutation(
            mediaId = 0,
            body = "Lorem ipsum dolor sit amet",
            score = 50,
            private = false,
            summary = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam sollicitudin condimentum nisi eu tempor. Proin dictum in met"
        )

        assertEquals(121, input.summary.length)

        val expected = input.isSummaryValid()
        assertFalse(expected)
    }

    @Test
    fun `given a summary that is one character less than the maximum requirement`() {
        val input = SaveReviewMutation(
            mediaId = 0,
            body = "Lorem ipsum dolor sit amet",
            score = 50,
            private = false,
            summary = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam sollicitudin condimentum nisi eu tempor.Proin dictum in me"
        )

        assertEquals(119, input.summary.length)

        val expected = input.isSummaryValid()
        assertTrue(expected)
    }

    @Test
    fun `given a summary that is greater than the maximum requirement`() {
        val input = SaveReviewMutation(
            mediaId = 0,
            body = "Lorem ipsum dolor sit amet",
            score = 50,
            private = false,
            summary = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam sollicitudin condimentum nisi eu tempor.
                Proin dictum in metus vitae laoreet. Sed consequat nulla vitae convallis elementum.
                Vestibulum fringilla vitae turpis in vehicula. Vestibulum facilisis eu neque non elementum.
                Vivamus eu nunc eu nisl lacinia euismod.

                In tempus, nunc eget aliquet mollis, purus purus ullamcorper neque, vitae ultrices ex quam non arcu.
                Phasellus porttitor rutrum tellus id venenatis. Aliquam luctus interdum tristique.
            """.trimIndent()
        )

        val expected = input.isSummaryValid()
        assertFalse(expected)
    }

    @Test
    fun `given a body that is greater than the minimum requirement and less than the maximum`() {
        val input = SaveReviewMutation(
            mediaId = 0,
            body = """
                 Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nam sollicitudin condimentum nisi eu tempor.
                 Proin dictum in metus vitae laoreet. Sed consequat nulla vitae convallis elementum. Vestibulum fringilla vitae turpis in vehicula.
                 Vestibulum facilisis eu neque non elementum. Vivamus eu nunc eu nisl lacinia euismod.
                 In tempus, nunc eget aliquet mollis, purus purus ullamcorper neque, vitae ultrices ex quam non arcu.
                 Phasellus porttitor rutrum tellus id venenatis. Aliquam luctus interdum tristique.

                Quisque euismod dui eget leo feugiat ullamcorper sed at magna. Vestibulum nec interdum risus, vel aliquet nunc.
                Phasellus maximus sem sed odio dignissim dapibus at vitae augue. Morbi commodo diam in pulvinar congue. Vivamus quis ipsum nunc.
                Nullam lectus lorem, fringilla viverra posuere nec, elementum quis justo. Phasellus molestie elementum nunc, id scelerisque lorem feugiat ac.
                Duis a volutpat augue. Nam urna eros, dictum a malesuada quis, ultricies auctor mi. Nam ut mollis dolor.
                In cursus est a accumsan viverra. Phasellus blandit lorem enim, vel viverra massa eleifend et.
                Nunc eros dui, vestibulum sed laoreet eget, vehicula ut lectus. Morbi sit amet ante nibh.

                Nulla hendrerit ante ut nibh tempus feugiat. Vivamus eget eros at diam efficitur gravida.
                Pellentesque rhoncus molestie mauris, at congue elit ornare in. Etiam egestas sodales elit eget luctus.
                Sed dui dui, gravida sit amet ante id, suscipit fermentum dolor. Maecenas rutrum tincidunt metus eget ornare.
                Cras ullamcorper, quam vitae bibendum lacinia, dui augue imperdiet nibh, in sollicitudin ante sapien sit amet eros.
                Donec vestibulum feugiat nibh ac ornare. Donec eget magna orci. Suspendisse eu erat semper, scelerisque mi vel, blandit purus.
                Sed rhoncus, felis sit amet rutrum dictum, lectus odio rutrum justo, non cursus ligula enim eu odio. Praesent vitae interdum est.

                Pellentesque imperdiet neque in faucibus elementum. Nulla hendrerit convallis sollicitudin.
                Pellentesque placerat nisi blandit mi malesuada, ut molestie tortor congue.
                Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae;
                Maecenas fringilla tincidunt ornare. Etiam vehicula et diam eget molestie.
                Mauris pharetra non eros vitae pharetra. Donec purus magna, semper vitae mi id, convallis aliquam lorem.
                Aenean enim est, posuere sit amet venenatis et, laoreet nec enim. Praesent ut velit nisl.
                Fusce pharetra orci vulputate lorem accumsan volutpat. Nullam ac fermentum justo.
            """.trimIndent(),
            score = 50,
            private = false,
            summary = "Short summary which should pass the test"
        )

        assertTrue(input.body.length > 1500)

        val expected = input.isBodyValid()
        assertTrue(expected)
    }

    @Test
    fun `given a body that is equal to the minimum requirement and less than the maximum`() {
        val input = SaveReviewMutation(
            mediaId = 0,
            body = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida magna mi a libero. Fusce vulputate eleifend sapien. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, met",
            score = 50,
            private = false,
            summary = "Short summary which should pass the test"
        )

        assertTrue(input.body.length == 1500)

        val expected = input.isBodyValid()
        assertTrue(expected)
    }

    @Test
    fun `given a body that is one character less than the minimum requirement and less than the maximum`() {
        val input = SaveReviewMutation(
            mediaId = 0,
            body = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida magna mi a libero. Fusce vulputate eleifend sapien. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, me",
            score = 50,
            private = false,
            summary = "Short summary which should pass the test"
        )

        assertTrue(input.body.length == 1499)

        val expected = input.isBodyValid()
        assertFalse(expected)
    }

    @Test
    fun `given a body that is one character more than the minimum requirement and less than the maximum`() {
        val input = SaveReviewMutation(
            mediaId = 0,
            body = "Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida magna mi a libero. Fusce vulputate eleifend sapien. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, mets",
            score = 50,
            private = false,
            summary = "Short summary which should pass the test"
        )

        assertTrue(input.body.length == 1501)

        val expected = input.isBodyValid()
        assertTrue(expected)
    }
}