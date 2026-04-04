package co.anitrend.media.component.compose.people

import co.anitrend.domain.character.enums.CharacterRole
import co.anitrend.domain.common.entity.shared.CoverImage
import co.anitrend.domain.common.entity.shared.CoverName
import co.anitrend.domain.media.entity.MediaPerson
import co.anitrend.domain.staff.enums.StaffLanguage
import kotlin.test.Test
import kotlin.test.assertEquals

class MediaPeoplePreviewSupportTest {

    @Test
    fun `selectCharacterPreview prioritizes main and supporting roles before background`() {
        val characters =
            listOf(
                character(id = 1, role = CharacterRole.BACKGROUND, hasImage = true),
                character(id = 2, role = CharacterRole.MAIN, hasImage = false),
                character(id = 3, role = CharacterRole.MAIN, hasImage = true),
                character(id = 4, role = CharacterRole.BACKGROUND, hasImage = false),
                character(id = 5, role = CharacterRole.SUPPORTING, hasImage = true),
            )

        val preview = selectCharacterPreview(characters, maxCount = 4)

        assertEquals(listOf(2L, 3L, 5L, 1L), preview.map { it.id })
    }

    @Test
    fun `selectCharacterPreview degrades gracefully when only background roles are available`() {
        val characters =
            listOf(
                character(id = 1, role = CharacterRole.BACKGROUND, hasImage = false),
                character(id = 2, role = CharacterRole.BACKGROUND, hasImage = true),
                character(id = 3, role = CharacterRole.BACKGROUND, hasImage = true),
            )

        val preview = selectCharacterPreview(characters, maxCount = 3)

        assertEquals(listOf(1L, 2L, 3L), preview.map { it.id })
    }

    @Test
    fun `selectStaffPreview prioritizes creator and director role groups before fallback credits`() {
        val staff =
            listOf(
                staff(id = 1, role = "Producer"),
                staff(id = 2, role = "Chief Director"),
                staff(id = 3, role = "Original Story"),
                staff(id = 4, role = "Music"),
                staff(id = 5, role = "Animation Assistant"),
            )

        val preview = selectStaffPreview(staff, maxCount = 4)

        assertEquals(listOf(3L, 2L, 1L, 4L), preview.map { it.id })
    }

    @Test
    fun `selectStaffPreview keeps source order within the same editorial priority band`() {
        val staff =
            listOf(
                staff(id = 1, role = "Writer"),
                staff(id = 2, role = "Composer"),
                staff(id = 3, role = "Music"),
            )

        val preview = selectStaffPreview(staff, maxCount = 3)

        assertEquals(listOf(1L, 2L, 3L), preview.map { it.id })
    }

    private fun character(
        id: Long,
        role: CharacterRole,
        hasImage: Boolean,
    ) =
        MediaPerson.Character(
            role = role,
            mediaRoleName = null,
            voiceActors = emptyList(),
            image =
                if (hasImage) {
                    CoverImage(
                        large = "https://example.com/$id-large.jpg",
                        medium = "https://example.com/$id-medium.jpg",
                    )
                } else {
                    null
                },
            name = previewName("Character $id"),
            siteUrl = null,
            id = id,
        )

    private fun staff(
        id: Long,
        role: String,
    ) =
        MediaPerson.Staff(
            role = role,
            language = StaffLanguage.JAPANESE,
            image = null,
            name = previewName("Staff $id"),
            siteUrl = null,
            id = id,
        )

    private fun previewName(name: String) =
        CoverName(
            middle = null,
            alternativeSpoiler = emptyList(),
            alternative = emptyList(),
            first = name.substringBefore(' ').takeIf(String::isNotBlank),
            full = name,
            last = name.substringAfter(' ', missingDelimiterValue = "").takeIf(String::isNotBlank),
            native = null,
            userPreferred = name,
        )
}
