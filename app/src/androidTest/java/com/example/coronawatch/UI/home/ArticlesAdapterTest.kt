package com.example.coronawatch.UI.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.coronawatch.Activities.MainActivity



import com.example.coronawatch.R
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
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
        onView(withId(R.id.viewPager)).perform(swipeLeft())
        Thread.sleep(1000)
        onView(withId(R.id.username_login)).perform(typeText("tester@esi.dz"))
        onView(withId(R.id.password_login)).perform(typeText("test"))
        onView(withId(R.id.loginbtn)).perform(click())
        Thread.sleep(3000)

    }
    @Test
    fun UserCanComment() {

        /*onView(withId(R.id.rv_home)).
            perform(RecyclerViewActions.actionOnItem(hasDescendant(allOf(withId(R.id.comment_content))),
                typeText("A comment from Test function : UserCanComment() ")))
        onView(withId(R.id.rv_home)).
            perform(RecyclerViewActions.actionOnItem(hasDescendant(allOf(withId(R.id.comment_content))),
                typeText("A comment from Test function : UserCanComment() ")))

       onView(withId(R.id.comment_content)).perform(typeText("A comment from Test function : UserCanComment() "))
        onView(withId(R.id.submit_comment_button)).perform(click())*/

    }
    /*@After
    fun tearDown() {
    }*/
}