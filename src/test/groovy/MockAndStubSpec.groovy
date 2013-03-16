import spock.lang.FailsWith
import spock.lang.Specification

class MockAndStubSpec extends Specification {

    @FailsWith(AssertionError)
    def "MockとStubを別のインタラクションとして宣言できない"() {
        setup:
        def publisher = new Publisher()

        and:
        def subscriber = Mock(Subscriber)
        subscriber.receive("message1") >> true // thenでの宣言が優先されるためこのモックは無効
        publisher.subscribers << subscriber

        when:
        publisher.send("message1")

        then:
        1 * subscriber.receive("message1") // これは暗黙的にデフォルトのfalseを返す
    }
}

class Publisher {
    def subscribers = []
    def send(String message) {
        subscribers.each {
            assert it.receive(message)
        }
    }
}

interface Subscriber {
    Boolean receive(String message)
}