### Test Strategy

#### I decided to use **jmeter** tool for these purposes.

#### What did I set up?

**Target**: https://www.saucedemo.com/ — single `GET` request against the _login_ page.

_Why only one endpoint_: this is a simple application where authentication is validated in JavaScript, not on the server.
Anyway you can't access other pages without logging in (you will be redirected.
Note, that the default jmeter setup per http request is _follow redirects_, and I didn't remove it, so it would not result in an error for other requests as well.

**Number of threads (users)**: tried different values (100, 200, 300, 500, 700, 1000), but stopped with 700.
That's because when the values were below `700` nothing interesting was happening, all requests were successful, 
though response times showed a brief increase in the middle before stabilizing again.
While with `1000`, it was `429` error for all the requests straight away.
Nevertheless, with 700 I get both: the successful requests (`200`) and errors (`429`).

**Note**: I saved outcomes for both `500` and `700` users.

**Ramp-up period**: `600s` -> not all the users make requests at one instant at the very beginning.

**Duration** (from scheduler): `900s` -> it's longer than ramp-up so it gives 300s for concurrency.

**Loop Count**: `infinite` -> each user continues sending requests until the duration is cut.

**Think Time (timer)**: Uniform Random Timer, `500ms` delay + `0-1000ms` random range (so 500-1500ms between each request per user).
_Why_: without it, every thread fires requests back-to-back in a tight loop, which simulates a continuous request loop rather than a real user browsing. 
This would artificially inflate the request rate and could trigger the `429` rate limit due to raw throughput rather than concurrent-user count.

**Overall Conclusion**:
When running tess with 500 users, all responses were 200.
However, for a small part of the users the response time was significantly longer: 998ms compared with average of 35ms. 
Though, I believe such a big value occurred only once somewhere maybe due to network issues, because it does fit only for 1% of users.
Nevertheless, such a big value was only true for 1% of the users, because:
* for 99% of the users it was less than 103ms
* for 95% of the users it was less than 54ms
* for 90% of the users it was less than 45ms
So at some point the response time increased, but that was only for a small amount of users, so overall it's stable.

When running tess with 700 users, not all responses were 200, 52.03% were 429 (I achieved rate limit).
The average response time increased comparing to 500 users (102ms vs 35ms).
For a small part of the users the response time was also significantly longer: 18868ms compared with average of 102ms.
Though, I also believe such a big value occurred only once somewhere maybe due to network issues, because it does fit only for 1% of users.
Nevertheless, such a big value was only true for 1% of the users, because:
* for 99% of the users it was less than 369ms
* for 95% of the users it was less than 270ms
* for 90% of the users it was less than 219ms
So when dealing with bigger amount of users than 500, the response times slowed down a lot and 429 errors took place.


