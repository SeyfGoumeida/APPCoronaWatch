package com.example.coronawatch.UI.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
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

    @Test

    fun signin() {

        onView(withId(R.id.user_btn)).perform(click())
        onView(withId(R.id.viewPager)).perform(swipeLeft())
        Thread.sleep(1000)
        onView(withId(R.id.username_login)).perform(typeText("tester@esi.dz"))
        onView(withId(R.id.password_login)).perform(typeText("test"))
        onView(withId(R.id.loginbtn)).perform(click())
        Thread.sleep(3000)
        //onView(withId(R.id.recycler_id)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withText("A")), click()));

    }

    fun signp() {
        onView(withId(R.id.user_btn)).perform(click())
        val random = (0..100).random()
        onView(withId(R.id.username)).perform(typeText("test$random"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.email)).perform(typeText("test$random@esi.dz"), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText("test"),closeSoftKeyboard())
        onView(withId(R.id.password2)).perform(typeText("test"),closeSoftKeyboard())
        onView(withId(R.id.signupbtn))
            .perform(click())
    }
    /*@After
    fun tearDown() {
    }*/
}