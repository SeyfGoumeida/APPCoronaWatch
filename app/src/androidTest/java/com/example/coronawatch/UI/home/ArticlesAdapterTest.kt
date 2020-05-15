package com.example.coronawatch.UI.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.example.coronawatch.Activities.MainActivity
import com.example.coronawatch.R
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticlesAdapterTest {
    @Rule
    @JvmField
    val rule :ActivityTestRule<MainActivity> =ActivityTestRule(MainActivity::class.java)
    @Before
    fun signin() {
        onView(withId(R.id.user_btn)).perform(click())
        onView(withId(R.id.tabs)).perform((click()))

        onView(withId(R.id.username)).perform(typeText("Test@esi.dz"))
        onView(withId(R.id.password)).perform(typeText("test"))
        onView(withId(R.id.loginbtn)).perform(click())

    }
    @Test
    fun UserCanComment() {
        onView(withId(R.id.comment_content)).perform(typeText("A comment from Test function : UserCanComment() "))
        onView(withId(R.id.submit_comment_button)).perform(click())

    }
    @After
    fun tearDown() {
    }
}