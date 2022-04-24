package com.shushant.messengercompose.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.shushant.messengercompose.R
import com.shushant.messengercompose.extensions.SearchComposable
import com.shushant.messengercompose.ui.theme.black20Bold
import com.shushant.messengercompose.ui.theme.gray20Bold
import io.github.farhanroy.cccp.CCPCountry
import io.github.farhanroy.cccp.getFlagMasterResID
import io.github.farhanroy.cccp.getLibraryMasterCountriesEnglish
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CountryCodePicker(pickedCountry: (CCPCountry) -> Unit) {
    val countryList: MutableList<CCPCountry> = getLibraryMasterCountriesEnglish().toMutableList()
    val currentCountry =
        getLibraryMasterCountriesEnglish().find { it.nameCode == Locale.current.region.lowercase() }
            ?: countryList[0]
    val picked = remember { mutableStateOf(currentCountry) }
    val textState = remember { mutableStateOf(TextFieldValue("")) }

    MaterialTheme {
        Column {
            val openDialog = remember { mutableStateOf(false) }
            Row(
                Modifier.clickable { openDialog.value = true },
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularResourceImage(
                    resourceId = getFlagMasterResID(picked.value),
                    size = 30
                )
                SizedBox(
                    width = 5
                )
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = stringResource(id = R.string.image_description),
                    modifier = Modifier.size(15.dp),
                    tint = Color.Gray
                )
                Text(
                    "+${picked.value.phoneCode}",
                    style = gray20Bold
                )
            }

            if (openDialog.value) {
                Dialog(onDismissRequest = { openDialog.value = false }) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        SearchComposable(textState)
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                        ) {
                            val searchedText = textState.value.text
                            val filterList =
                                if (searchedText.isEmpty()) countryList else countryList.filter {
                                    it.name.contains(searchedText,true)
                                }
                            LazyColumn {
                                items(filterList.size) { index ->
                                    Row(
                                        Modifier
                                            .padding(
                                                horizontal = 18.dp,
                                                vertical = 18.dp
                                            )
                                            .clickable {
                                                pickedCountry(filterList[index])
                                                picked.value = filterList[index]
                                                openDialog.value = false
                                            }) {
                                        CircularResourceImage(
                                            resourceId = getFlagMasterResID(filterList[index]),
                                            size = 30
                                        )
                                        Text(
                                            filterList[index].name,
                                            style = black20Bold,
                                            modifier = Modifier.padding(horizontal = 18.dp)
                                        )
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }

    }
}