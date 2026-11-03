package com.jarvis.ai.action

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class PlanParserTest {

    private val parser = PlanParser()

    @Test
    fun emptyString_returnsEmptyPlan() {
        assertThat(parser.parse("").isEmpty).isTrue()
        assertThat(parser.parse("   ").isEmpty).isTrue()
    }

    @Test
    fun nonJson_returnsEmptyPlan() {
        assertThat(parser.parse("hello there").isEmpty).isTrue()
    }

    @Test
    fun emptyActionsArray_returnsEmpty() {
        val plan = parser.parse("""{"actions":[]}""")
        assertThat(plan.isEmpty).isTrue()
    }

    @Test
    fun parsesOpenApp() {
        val raw = """
            {"actions":[{"type":"open_app","params":{"appName":"YouTube"}}]}
        """.trimIndent()
        val plan = parser.parse(raw)
        assertThat(plan.actions).hasSize(1)
        val a = plan.actions[0].action
        assertThat(a).isInstanceOf(AssistantAction.OpenApp::class.java)
        assertThat((a as AssistantAction.OpenApp).appName).isEqualTo("YouTube")
        assertThat(a.requiresConfirmation).isFalse()
    }

    @Test
    fun parsesReminder_withRequiresConfirmationTrue() {
        val raw = """
            {"actions":[{"type":"create_reminder","params":{
                "title":"Call mom","isoTime":"2030-01-01T10:00:00+00:00"
            }}]}
        """.trimIndent()
        val plan = parser.parse(raw)
        val a = plan.actions.single().action
        assertThat(a).isInstanceOf(AssistantAction.CreateReminder::class.java)
        assertThat((a as AssistantAction.CreateReminder).title).isEqualTo("Call mom")
        assertThat(a.requiresConfirmation).isTrue()
    }

    @Test
    fun invalidUrlIsRejected() {
        val raw = """{"actions":[{"type":"open_url","params":{"url":"javascript:alert(1)"}}]}"""
        assertThat(parser.parse(raw).isEmpty).isTrue()
    }

    @Test
    fun validHttpsUrlIsAccepted() {
        val raw = """{"actions":[{"type":"open_url","params":{"url":"https://example.com"}}]}"""
        val plan = parser.parse(raw)
        assertThat(plan.actions).hasSize(1)
        assertThat((plan.actions[0].action as AssistantAction.OpenUrl).url)
            .isEqualTo("https://example.com")
    }

    @Test
    fun codeFencedJson_isStripped() {
        val raw = """
            ```json
            {"actions":[{"type":"open_app","params":{"appName":"Maps"}}]}
            ```
        """.trimIndent()
        val plan = parser.parse(raw)
        assertThat(plan.actions).hasSize(1)
    }

    @Test
    fun unknownTypeIsIgnored() {
        val raw = """{"actions":[{"type":"run_shell","params":{"cmd":"rm -rf /"}}]}"""
        assertThat(parser.parse(raw).isEmpty).isTrue()
    }
}