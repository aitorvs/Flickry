package com.example.aitor.flickry.api;

import android.support.test.runner.AndroidJUnit4;
import com.example.aitor.flickry.AppObjectGraph;
import com.example.aitor.flickry.Resource;
import com.example.aitor.flickry.photos.FlickrData;
import com.example.aitor.flickry.photos.Photo;
import com.example.aitor.flickry.photos.PhotoRequest;
import com.nytimes.android.external.store3.base.impl.Store;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import retrofit2.adapter.rxjava2.Result;

import static com.example.aitor.flickry.ObjectGraphStore.onObjectGraph;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class PhotoServiceTest {
    private PhotoService service;
    private Store<Resource<FlickrData>, PhotoRequest> store;
    private final PhotoRequest search = PhotoRequest.create("kittens", 1);

    @Before
    public void setup() {
        service = onObjectGraph(AppObjectGraph.class).photoService();
        store = onObjectGraph(AppObjectGraph.class).photoStore();
    }

    @Test
    public void getKittens_noError() {
        Result<JsonResponse> r = service.getPhotosOf(search).blockingGet();
        assertFalse(String.format("Retrofit error %s", r.error()), r.isError());
        assertNotNull("response == null", r.response());
        assertTrue(String.format("HTTP error code %d", r.response().code()), r.response().isSuccessful());
        assertNotNull("body == null", r.response().body());

        JsonResponse response = r.response().body();
        JsonPhotos photos = response.photos();
        assertNotNull("photos == null", photos);
        assertTrue(photos.total() > 0);
        assertTrue("page != 1",photos.page() == 1);
        // FIXME the value 200 is hardcoded and should be fixed in the future
        assertTrue("perpage != 200",photos.perPage() == 200);
    }

    @Test
    public void fetchPhotoStore_notEmpty() {
        Resource<FlickrData> resource = store.fetch(search).blockingGet();

        assertFalse(resource.isError());
        assertTrue(resource.isSuccess());
        assertNotNull("data == null", resource.data);
        assertFalse("empty list", resource.data.photos().isEmpty());
    }

    @Test
    public void photoBuilder() {
        String url = "http://farm1.static.flickr.com/1234/12345678_abcde.jpg";
        Photo actual = Photo.builder().farm(1).server("1234").id("12345678").secret("abcde").build();
        assertEquals(url, actual.url());
    }
}
