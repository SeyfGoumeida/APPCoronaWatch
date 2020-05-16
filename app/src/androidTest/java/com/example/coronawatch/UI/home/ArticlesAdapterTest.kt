package com.example.coronawatch.UI.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.coronawatch.Activities.MainActivity
import com.example.coronawatch.R
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
   fun CommentingProccess () {

        val random = (0..100).random()
        val username = "test$random"
        val email = "test$random@esi.dz"
        val password = "test"

        SignUp(username , email  , password)
        LogOut()
        SignIn(email , password)
        UserCanComment("trying the whole thing yay")
        LogOut()


    }


    fun SignUp(username: String , email:String , password: String) {

        onView(withId(R.id.user_btn)).perform(click())
        onView(withId(R.id.username)).perform(typeText(username), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.email)).perform(typeText(email), ViewActions.closeSoftKeyboard())
        onView(withId(R.id.password)).perform(typeText(password),closeSoftKeyboard())
        onView(withId(R.id.password2)).perform(typeText(password),closeSoftKeyboard())
        onView(withId(R.id.signupbtn)).perform(click())
        Thread.sleep(3000)

        return
    }


    fun SignIn( username : String , password : String) {

        onView(withId(R.id.user_btn)).perform(click())
        onView(withId(R.id.viewPager)).perform(swipeLeft())
        Thread.sleep(1000)
        onView(withId(R.id.username_login)).perform(typeText(username))
        onView(withId(R.id.password_login)).perform(typeText(password))
        onView(withId(R.id.loginbtn)).perform(click())
        Thread.sleep(3000)

    }


    fun UserCanComment( comment : String) {

        fun type(viewId: Int , comment :String) = object : ViewAction {
            override fun getConstraints() = null

            override fun getDescription() = "Click on a child view with specified id."

            override fun perform(uiController: UiController, view: View) =
                typeText(comment).perform(
                    uiController,
                    view.findViewById<View>(viewId)
                )
        }

        fun click(viewId: Int ) = object : ViewAction {
            override fun getConstraints() = null

            override fun getDescription() = "Click on a child view with specified id."

            override fun perform(uiController: UiController, view: View) =
                click().perform(uiController, view.findViewById<View>(viewId))

        }

        onView(withId(R.id.rv_home))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    type(R.id.comment_content, comment)
                )
            )

        onView(withId(R.id.rv_home))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click(R.id.submit_comment_button)
                )
            )

        onView(withId(R.id.rv_home)).perform(swipeUp());
        Thread.sleep(2000)
        onView(withId(R.id.rv_home)).perform(swipeUp());
        Thread.sleep(2000)
        onView(withId(R.id.rv_home)).perform(swipeUp());
        Thread.sleep(2000)


    }


        fun LogOut() {

        onView(withId(R.id.navigation_profil)).perform(click())
            Thread.sleep(4000)
        onView(withId(R.id.logout)).perform(click())
        Thread.sleep(4000)



    }
    }
