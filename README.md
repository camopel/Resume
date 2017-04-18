# Resume Android App
This is a resume app on android platform.

personal resume data is load by http request from aws server as json format. Personal photo, name, phone and address are show on the navigation menu header. experience are show on time view and map view. 

Time view show each experience including education and work experience sorted by start time in ascending trend. Each item in this list can be clicked and then it will display more details including description and address. the address is clickable, will jump to google map and anchor to its address.

Map view show each marker with organizaition logo, name and title on the google map, the whole area is bounded to include locations of all items. each marker is clickable and will lead to the detail page view. The map view can be zoomed or show your current location by GPS data.

Search function is supported in the main view, the search input will show up after click the search icon, input will be searched among title, org and description 
