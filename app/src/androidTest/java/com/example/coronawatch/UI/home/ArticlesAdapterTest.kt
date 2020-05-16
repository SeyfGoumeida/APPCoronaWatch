package com.example.coronawatch.UI.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.coronawatch.Activities.MainActivity
import com.example.coronawatch.R
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

        fun type(viewId: Int) = object : ViewAction {
            override fun getConstraints() = null

            override fun getDescription() = "Click on a child view with specified id."

            override fun perform(uiController: UiController, view: View) = typeText("A comment from Test function : UserCanComment() ").
                perform(uiController, view.findViewById<View>(viewId))
        }

        fun clickOnViewChild(viewId: Int) = object : ViewAction {
            override fun getConstraints() = null

            override fun getDescription() = "Click on a child view with specified id."

            override fun perform(uiController: UiController, view: View) = click().perform(uiController, view.findViewById<View>(viewId))

        }

        onView(withId(R.id.rv_home))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                type(R.id.comment_content)))

        onView(withId(R.id.rv_home))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,
                clickOnViewChild(R.id.submit_comment_button)))


    }
    /*@After
    fun tearDown() {
    }*/
}