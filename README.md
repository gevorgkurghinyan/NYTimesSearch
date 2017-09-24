# Project 2 - NYTimesSearch

NYTimesSearch is an android app that allows a user to search for articles on web using simple filters. The app utilizes [New York Times Search API](http://developer.nytimes.com/docs/read/article_search_api_v2).

Time spent: 22 hours spent in total

## User Stories

The following **required** functionality is completed:

* User can **search for news article** by specifying a query and launching a search. Search displays a grid of image results from the New York Times Search API.
* User can click on "settings" which allows selection of **advanced search options** to filter results
* User can configure advanced search filters such as:
  * Begin Date (using a date picker)
  * News desk values (Arts, Fashion & Style, Sports)
  * Sort order (oldest or newest)
* Subsequent searches have any filters applied to the search results
* User can tap on any article in results to view the contents in an embedded browser.
* User can **scroll down to see more articles**. The maximum number of articles is limited by the API search.

The following **optional** features are implemented:

* Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
* Used the **ActionBar SearchView** or custom layout as the query box instead of an EditText
* User can **share an article link** to their friends or email it to themselves
* Replaced Filter Settings Activity with a lightweight modal overlay

The following **bonus** features are implemented:

* Use the [RecyclerView](http://guides.codepath.com/android/Using-the-RecyclerView) with the `StaggeredGridLayoutManager` to display improve the grid of image results
* [Used android.os.Parcelable] Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.com/android/Using-Parceler).
* Leverages the [data binding support module](http://guides.codepath.com/android/Applying-Data-Binding-for-Views) to bind data into layout templates.
* Replace Picasso with [Glide](http://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en) for more efficient image rendering.
* Leverages the popular [GSON library](http://guides.codepath.com/android/Using-Android-Async-Http-Client#decoding-with-gson-library) to streamline the parsing of JSON data.
* Leverages the [Retrofit networking library](http://guides.codepath.com/android/Consuming-APIs-with-Retrofit) to access the New York Times API.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='https://i.imgur.com/1p6ceHL.gifv' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.

## Open-source libraries used

- [Retrofit](http://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java
- [Glide](https://github.com/bumptech/glide/) - Image loading and caching library for Android
- [Gson] (https://github.com/google/gson) - Gson is a Java library that can be used to convert Java Objects into their JSON representation. It can also be used to convert a JSON string to an equivalent Java object.
- [Butterknife] (http://jakewharton.github.io/butterknife/) - Annotate fields with @BindView and a view ID for Butter Knife to find and automatically cast the corresponding view in your layout.

## License

    Copyright [2017] [Gevorg Kurghinyan]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
