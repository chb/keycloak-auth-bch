# keycloak-auth

OAuth2 authentication to manage Keycloak tokens (it needs a Keycloak url, realm and client_id to work) to allow login based on user and password. It exposes an API with these operations:

- [POST] login
- [GET] user

## Next release
#### GitHub

**Use https instead git in mavan scm preparing the release**


[820fbb9ef9afcc4](https://github.com/arihealth/keycloak-auth/commit/820fbb9ef9afcc4) Carlos Cavero *2019-07-26 00:17:37*

**Avoid compiling every branch in Travis**


[0dc52737f5f98b8](https://github.com/arihealth/keycloak-auth/commit/0dc52737f5f98b8) Carlos Cavero *2019-07-26 00:17:04*


## 0.0.1

#### GitHub #13 Update documentation 

**Merge pull request #13 from AriHealth/documentation-package**

 * Update documentation 

[fb2d138fe304604](https://github.com/arihealth/keycloak-auth/commit/fb2d138fe304604) Carlos Cavero *2019-06-19 11:55:58*



#### GitHub #16 Documentation 15 headers license disclaimer   *question*  

**Merge pull request #16 from AriHealth/documentation-15-headersLicenseDisclaimer**

 * Documentation 15 headers license disclaimer 

[26b4b5fb51da6f4](https://github.com/arihealth/keycloak-auth/commit/26b4b5fb51da6f4) mariorguez *2019-07-03 07:08:56*



#### GitHub #18 Feature 3 add travis ci   *enhancement*  

**Merge pull request #18 from AriHealth/feature-3-addTravisCI**

 * Feature 3 add travis ci 

[8beb7255058c3d8](https://github.com/arihealth/keycloak-auth/commit/8beb7255058c3d8) Carlos Cavero *2019-07-25 22:52:05*



#### GitHub #19 bugfix: Put the correct .travis folder in the yml file 

**Merge pull request #19 from AriHealth/bugfix-travisFolder**

 * bugfix: Put the correct .travis folder in the yml file 

[4b5cac2f5f82f92](https://github.com/arihealth/keycloak-auth/commit/4b5cac2f5f82f92) Carlos Cavero *2019-07-25 23:25:04*



#### GitHub #20 Exclude .travis folder during the prepare stage and git add only the …   *bug*  

**Merge pull request #20 from AriHealth/bugfix-excludeTravisFolderRelase**

 * Exclude .travis folder during the prepare stage and git add only the … 

[da380a01b191794](https://github.com/arihealth/keycloak-auth/commit/da380a01b191794) Carlos Cavero *2019-07-26 00:00:00*



#### GitHub #4 Structure properly the project and reshaping the classes 

**Merge pull request #4 from AriHealth/feature-1-configClientId**

 * Structure properly the project and reshaping the classes 

[401cbd41cd42a09](https://github.com/arihealth/keycloak-auth/commit/401cbd41cd42a09) Carlos Cavero *2019-06-04 15:29:46*



#### GitHub #5 Add docker support to the spring project with environment variables   *enhancement*  

**Merge pull request #5 from AriHealth/feature-2-dockerSupport**

 * Add docker support to the spring project with environment variables 

[33ba92bd480cfcc](https://github.com/arihealth/keycloak-auth/commit/33ba92bd480cfcc) Carlos Cavero *2019-06-05 13:19:57*



#### GitHub #8 Add logback support, separating dev from prod logging level   *bug*  *enhancement*  

**Merge pull request #8 from AriHealth/feature-6-logLevelFiltering**

 * Add logback support, separating dev from prod logging level 

[3adf1f16f67d066](https://github.com/arihealth/keycloak-auth/commit/3adf1f16f67d066) Carlos Cavero *2019-06-10 14:04:58*



#### GitHub #9 Refactor based on sonar recommendations 

**Merge pull request #9 from AriHealth/refactor-ide-warnings**

 * Refactor based on sonar recommendations 

[016c12395475159](https://github.com/arihealth/keycloak-auth/commit/016c12395475159) Carlos Cavero *2019-06-13 13:48:26*


#### GitHub

**Exclude .travis folder during the prepare stage and git add only the CHANGELOG and the pom files**


[5d9863045f2bb92](https://github.com/arihealth/keycloak-auth/commit/5d9863045f2bb92) Carlos Cavero *2019-07-25 23:51:32*

**Automatic release increment and changelog generation. Travis build $TRAVIS_BUILD_NUMBER pushed [skip ci]**


[3cf135ee1bcc47e](https://github.com/arihealth/keycloak-auth/commit/3cf135ee1bcc47e) Carlos *2019-07-25 23:35:46*

**Put the proper url travis badge in the README**


[c02dd760b003e4e](https://github.com/arihealth/keycloak-auth/commit/c02dd760b003e4e) Carlos Cavero *2019-07-25 23:32:03*

**Put the correct .travis folder in the yml file**


[b40caec4f80740e](https://github.com/arihealth/keycloak-auth/commit/b40caec4f80740e) Carlos Cavero *2019-07-25 23:14:25*

**Move the jacoco plugin outside PluginManagement to generate the jacoco.exec report**


[eb32b9485331091](https://github.com/arihealth/keycloak-auth/commit/eb32b9485331091) Carlos Cavero *2019-07-25 22:26:55*

**Add the option -s to include the maven setting in sonar phase**


[a90ed96c5d83c31](https://github.com/arihealth/keycloak-auth/commit/a90ed96c5d83c31) Carlos Cavero *2019-07-25 21:00:25*

**Modify travis removing old references**


[0d032721da7c1bd](https://github.com/arihealth/keycloak-auth/commit/0d032721da7c1bd) Carlos Cavero *2019-07-25 20:52:17*

**Add travis yml for the Continuous Integration of the project**


[450644b95af9741](https://github.com/arihealth/keycloak-auth/commit/450644b95af9741) Carlos Cavero *2019-07-25 20:28:33*

**Add maven dependencies and builds**


[7e699123a2dfe12](https://github.com/arihealth/keycloak-auth/commit/7e699123a2dfe12) Carlos Cavero *2019-07-25 20:24:43*

**Add the changelog configuration files**


[e393ad1101fe525](https://github.com/arihealth/keycloak-auth/commit/e393ad1101fe525) Carlos Cavero *2019-07-25 20:24:00*

**Upload the travis scripts for git and settings for maven**


[6c3a266731b8b8f](https://github.com/arihealth/keycloak-auth/commit/6c3a266731b8b8f) Carlos Cavero *2019-07-25 20:23:24*

**Add travis and codecov badges to README file**


[94e37718b65f555](https://github.com/arihealth/keycloak-auth/commit/94e37718b65f555) Carlos Cavero *2019-07-24 13:27:36*

**Update doc**


[1a45d60cc2a2e97](https://github.com/arihealth/keycloak-auth/commit/1a45d60cc2a2e97) J. Mario Rguez *2019-07-10 09:19:21*

**Add disclaimer, license agreement and clause to the README**


[510bbe6ac686588](https://github.com/arihealth/keycloak-auth/commit/510bbe6ac686588) Carlos Cavero *2019-07-02 11:30:56*

**Add Apache 2.0 header to Dockerfile**


[1c599559387c2e8](https://github.com/arihealth/keycloak-auth/commit/1c599559387c2e8) Carlos Cavero *2019-07-02 11:28:43*

**Update documentation**

 * Minor clarifications in description, technology and deploy instructions. It solves also the KeyCloak typo. 

[7af63749a950673](https://github.com/arihealth/keycloak-auth/commit/7af63749a950673) J. Mario Rguez *2019-06-19 10:58:30*

**Update documentation**


[54f497081df3176](https://github.com/arihealth/keycloak-auth/commit/54f497081df3176) J. Mario Rguez *2019-06-12 17:05:04*

**Indent source code**


[06ba2c424a2e034](https://github.com/arihealth/keycloak-auth/commit/06ba2c424a2e034) J. Mario Rguez *2019-06-12 11:31:08*

**Adapt coding recommendations to variables, log and conditionals**


[c3ef50c0d9e9805](https://github.com/arihealth/keycloak-auth/commit/c3ef50c0d9e9805) J. Mario Rguez *2019-06-12 11:02:29*

**Remove author tag in source code header**


[879835d4448adef](https://github.com/arihealth/keycloak-auth/commit/879835d4448adef) J. Mario Rguez *2019-06-12 11:00:29*

**Add serial id to NotAuthorizedException**


[8472b4fdcd4e9d2](https://github.com/arihealth/keycloak-auth/commit/8472b4fdcd4e9d2) J. Mario Rguez *2019-06-12 09:44:48*

**Add schema to logback xml config file**


[d751968b0292654](https://github.com/arihealth/keycloak-auth/commit/d751968b0292654) J. Mario Rguez *2019-06-12 09:43:54*

**Remove unused import**


[e36d40e1e212a25](https://github.com/arihealth/keycloak-auth/commit/e36d40e1e212a25) J. Mario Rguez *2019-06-12 09:43:38*

**Add logback support, separating dev from prod logging level**

 * - The &quot;Bearer&quot; string must be included at the beginning of the header in user function 

[f31944979fef649](https://github.com/arihealth/keycloak-auth/commit/f31944979fef649) Carlos Cavero *2019-06-10 13:50:42*

**Add docker support to the spring project with environment variables**


[111706f412ee16a](https://github.com/arihealth/keycloak-auth/commit/111706f412ee16a) Carlos Cavero *2019-06-05 13:13:51*

**Structure properly the project and reshaping the classes to access KeyCloak**


[670e026d028a5c9](https://github.com/arihealth/keycloak-auth/commit/670e026d028a5c9) Carlos Cavero *2019-06-04 15:25:52*

**Remove email from the headers to avoid phising**


[6da94542a7172ea](https://github.com/arihealth/keycloak-auth/commit/6da94542a7172ea) Carlos Cavero *2018-11-19 22:05:24*

**Improve the README with the KeyCloak configuration how-to**


[fade58aa99cfb1b](https://github.com/arihealth/keycloak-auth/commit/fade58aa99cfb1b) Carlos Cavero *2018-07-23 14:24:23*

**Modify public_key_heartman by public_key**


[c81fc2dd1027694](https://github.com/arihealth/keycloak-auth/commit/c81fc2dd1027694) Carlos Cavero *2018-07-23 14:22:42*

**Update the KeyCloak configuration and properties data**


[475e6cc2623a664](https://github.com/arihealth/keycloak-auth/commit/475e6cc2623a664) Carlos Cavero *2018-07-23 14:22:23*

**Manage KeyCloak OAuth 2.0 tokens with Lombok library**


[83917b0641d5f6e](https://github.com/arihealth/keycloak-auth/commit/83917b0641d5f6e) Carlos Cavero *2018-07-23 14:11:17*


