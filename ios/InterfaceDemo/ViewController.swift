import AVKit
import SourceInterface
import UIKit
import WebKit

// Create protocol.
// '@objc' keyword is required. because method call is based on ObjC.
@objc protocol JavaScriptInterface {
    func emit(_ listner: String, dictonary: [String: AnyObject])
    @objc optional func getErrorMessages(codes: [JSInt]) -> [String]
}

// Implement protocol.
extension ViewController: JavaScriptInterface {
    func emit(_ listner: String, dictonary _: [String: AnyObject]) {
        // do whatever you want to do with these params 1 - listner 2 - dictonary
        NSLog("listner \(listner)")
        // NSLog("dictonary \(dictonary)")
    }

    func getErrorMessages(codes: [JSInt]) -> [String] {
        codes.map { "message\($0)" }
    }
}

class ViewController: UIViewController {
    fileprivate var webView: WKWebView!
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        // Video
        let videoPath = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
                guard let url = URL(string: videoPath) else { return }
                let player = AVPlayer(url: url)
                player.rate = 1 // auto play
                let playerFrame = CGRect(x: 0, y: 0, width: view.frame.width, height: view.frame.height)
                let playerViewController = AVPlayerViewController()
                playerViewController.player = player
                playerViewController.view.frame = playerFrame
                addChild(playerViewController)
                view.addSubview(playerViewController.view)
                playerViewController.view.isOpaque = true
                playerViewController.didMove(toParent: self)
        
        // Create javaScriptController.
        let javaScriptController = SourceInterface(name: "SourceInterface", target: self, bridgeProtocol: JavaScriptInterface.self)
        // [Optional] Add your javascript.
        let configuration = WKWebViewConfiguration()
        let userContentController = WKUserContentController()
        configuration.userContentController = userContentController
        // Create WKWebView instance.
        webView = WKWebView(frame: CGRect(x: 0, y: 0, width: view.frame.width, height: view.frame.height), configuration: configuration)
        webView.uiDelegate = self
        view.addSubview(webView)
        // Assign javaScriptController.
        webView.javaScriptController = javaScriptController
        webView.prepareForJavaScriptController() // Call prepareForJavaScriptController
        let myURL = URL(string: "https://experience-dev.sourcesync.io/today-home-owner-overlay")
        let myRequest = URLRequest(url: myURL!)
        webView.isOpaque = false
        webView.load(myRequest)
    }
}

extension ViewController: WKUIDelegate {
    func webView(_: WKWebView, runJavaScriptAlertPanelWithMessage message: String, initiatedByFrame _: WKFrameInfo, completionHandler: @escaping () -> Void) {
        let alertController = UIAlertController(title: nil, message: message, preferredStyle: .alert)
        alertController.addAction(UIAlertAction(title: "OK", style: .default, handler: { _ in
            completionHandler()
        }))
        present(alertController, animated: true, completion: nil)
    }
}
