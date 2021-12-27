package com.plcoding.cleanarchitecturenoteapp.feature_note.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.plcoding.cleanarchitecturenoteapp.MainActivity
import com.plcoding.cleanarchitecturenoteapp.core.util.TestTags
import com.plcoding.cleanarchitecturenoteapp.di.AppModule
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.add_edit_note.AddEditNoteScreen
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.notes.NotesScreen
import com.plcoding.cleanarchitecturenoteapp.feature_note.presentation.util.Screen
import com.plcoding.cleanarchitecturenoteapp.ui.theme.CleanArchitectureNoteAppTheme
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@HiltAndroidTest
@UninstallModules(AppModule::class)
class NotesEndToEndTest {
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @ExperimentalAnimationApi
    @Before
    fun setUp() {
        hiltRule.inject()
        composeRule.setContent {
            CleanArchitectureNoteAppTheme {

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.NoteScreen.route
                ) {
                    composable(
                        route = Screen.NoteScreen.route
                    ) {
                        NotesScreen(navController = navController)
                    }
                    composable(
                        route = Screen.AddEditNoteScreen.route
                                + "?noteId={noteId}&noteColor={noteColor}",
                        arguments = listOf(
                            navArgument(
                                name = "noteId"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                            navArgument(
                                name = "noteColor"
                            ) {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        val color = it.arguments?.getInt("noteColor") ?: -1
                        AddEditNoteScreen(
                            navController = navController,
                            noteColor = color
                        )
                    }
                }
            }
        }
    }

    @Test
    fun saveNewNote_editAfterwards() {
        //Click on FAB to go to add Note screen
        composeRule.onNodeWithContentDescription("Add").performClick()

        //set title and content
        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .performTextInput("Test-Title")
        composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD)
            .performTextInput("Test-Content")

        //save the note
        composeRule.onNodeWithContentDescription("Save")
            .performClick()

        composeRule.onNodeWithText("Test-Title").assertIsDisplayed()

        composeRule.onNodeWithText("Test-Title").performClick()

        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD).assertTextEquals("Test-Title")
        composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD).assertTextEquals("Test-Content")


        composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
            .performTextInput("2")

        composeRule.onNodeWithContentDescription("Save").performClick()

        composeRule.onNodeWithText("Test-Title2").assertIsDisplayed()
    }


    @Test
    fun saveNewNotes_orderByTitleDescending() {
        for (i in 1..3) {
            //Click on FAB to go to add Note screen
            composeRule.onNodeWithContentDescription("Add").performClick()

            //set title and content
            composeRule.onNodeWithTag(TestTags.TITLE_TEXT_FIELD)
                .performTextInput("Test-Title $i")
            composeRule.onNodeWithTag(TestTags.CONTENT_TEXT_FIELD)
                .performTextInput("Test-Content $i")

            //save the note
            composeRule.onNodeWithContentDescription("Save")
                .performClick()
        }

        composeRule.onNodeWithText("Test-Title 1").assertIsDisplayed()
        composeRule.onNodeWithText("Test-Title 2").assertIsDisplayed()
        composeRule.onNodeWithText("Test-Title 3").assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription("Sort")
            .performClick()

        composeRule
            .onNodeWithContentDescription("Title")
            .performClick()

        composeRule
            .onNodeWithContentDescription("Descending")
            .performClick()

        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[0]
            .assertTextContains("Test-Content 3")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[1]
            .assertTextContains("Test-Content 2")
        composeRule.onAllNodesWithTag(TestTags.NOTE_ITEM)[2]
            .assertTextContains("Test-Content 1")
    }

}