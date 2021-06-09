<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://upload.wikimedia.org/wikipedia/commons/thumb/5/5a/Torch.svg/1200px-Torch.svg.png">
    <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/5/5a/Torch.svg/1200px-Torch.svg.png" alt="Logo" width="80" height="150">
  </a>

  <h3 align="center">ExTorch</h3>

  <p align="center">
    Testing malware for data exfiltration on Android devices
    <br />
    <!--<a href="https://github.com/gamirkola/ExTorch"><strong>Explore the docs »</strong></a>-->
    <br />
    <br />
  </p>
</p>



<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <!-- <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>-->
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <!-- <li><a href="#acknowledgements">Acknowledgements</a></li>-->
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

<!-- [![Product Name Screen Shot][product-screenshot]](https://example.com)-->
This project id made only for research purposes on exfiltration attacks

<!--
### Built With

* []()
* []()
* []()
-->


<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

* AndroidStudio
  ```
  https://developer.android.com/studio
  ```

* ADB

* Web server like Apache or Nginx

### Installation

1. Clone the repo
   ```sh
   git clone https://github.com/gamirkola/ExTorch.git
   ```
2. Open the project on AndroidStudio

3. Connect the device to the computer

4. Copy exfil_server folder into your web server folder
5. In src/main/java/com/mlw/extorch/MainActivity.java modify the var "base_url" writing your server address
6. start the app via AndroidStudio


<!-- USAGE EXAMPLES -->
## Usage

At the moment the app implements two functions, one to exfiltrate a simple file to the server and the most important one permits the exfilrtatia images from the phone via http
get request, sending those to the server.

<!-- ROADMAP -->
## Roadmap

See the [open issues](https://github.com/gamirkola/ExTorch/issues) for a list of proposed features (and known issues).

<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request


<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.



<!-- CONTACT -->
## Contact

Project Link: [https://github.com/gamirkola/ExTorch](https://github.com/gamirkola/ExTorch)



<!-- ACKNOWLEDGEMENTS -->
<!--
## Acknowledgements

* []()
* []()
* []()
-->





<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/gamirkola/repo.svg?style=for-the-badge
[contributors-url]: https://github.com/gamirkola/repo/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/gamirkola/repo.svg?style=for-the-badge
[forks-url]: https://github.com/gamirkola/repo/network/members
[stars-shield]: https://img.shields.io/github/stars/gamirkola/repo.svg?style=for-the-badge
[stars-url]: https://github.com/gamirkola/repo/stargazers
[issues-shield]: https://img.shields.io/github/issues/gamirkola/repo.svg?style=for-the-badge
[issues-url]: https://github.com/gamirkola/repo/issues
[license-shield]: https://img.shields.io/github/license/gamirkola/repo.svg?style=for-the-badge
[license-url]: https://github.com/gamirkola/repo/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/gamirkola
