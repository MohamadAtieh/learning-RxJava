import rx.Observable;
import rx.Subscriber;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Exact example as 9 but uses the filter() operator to filter out nulls
 * This example also explains the take() operator
 * Created by matie on 26/04/15.
 */
public class example10 {
    public static void main(String[] args){

        //use the filter operator before the final printout which is expected to skip i == 2
        query("rx-filter").flatMap(urls -> Observable.from(urls))
                .flatMap(url -> getTitle(url))
                .filter(title -> title != null)
                .subscribe(title -> System.out.println(title));

        System.out.println();

        //take(n) - reads the first n items
        query("rx-take").flatMap(urls -> Observable.from(urls))
                .flatMap(url -> getTitle(url))
                .filter(title -> title != null)
                .take(3)
                .subscribe(title -> System.out.println(title));

        System.out.println();
    }

    //We could have used just() here, but I want the subscriber to ONLY print out my modified text.
    private static Observable<String> getTitle(String Url){
        return Observable.create(subscriber -> mapUrlToTitle(subscriber, Url));
    }

    private static void mapUrlToTitle(Subscriber<? super String> subscriber, String url) {
        if(!subscriber.isUnsubscribed()){
            //return null if 404
            if(!url.equals("404")){
                subscriber.onNext("Example title for " + url);
            }else{
                subscriber.onNext(null);
            }

            subscriber.onCompleted();
        }
    }

    /**
     * Re-using the query method in this example
     * I prefer the lambda expressions as they are easier to extend. ANOTHER PLUS.
     */
    private static Observable<List<String>> query(String text) {
        return Observable.create(subscriber -> createAndEmitList(subscriber, text));
    }

    //I only have to deal with this part of the code and keep our query method untouched.
    // If i == 2, I will print 404.
    private static void createAndEmitList(Subscriber<? super List<String>> subscriber, String text){
        List<String> myList = new ArrayList<String>();
        for(int i = 0 ; i < 6 ; i++){
            if(i == 2){
                myList.add(404+"");
                continue;
            }
            myList.add(text+ i + ".com");
        }
        if(!subscriber.isUnsubscribed()){
            subscriber.onNext(myList);
            subscriber.onCompleted();
        }
    }

}
