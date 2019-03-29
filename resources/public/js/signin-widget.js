customSignIn = function () {
     var signIn = new OktaSignIn({baseUrl: 'https://in-progress.okta.com'});
     signIn.renderEl({
       el: '#widget-container'
     }, function success(res) {
       if (res.status === 'SUCCESS') {
         console.log('Do something with this sessionToken', res.session.token);
       } else {
         console.log('Failed login');
       // The user can be in another authentication state that requires further action.
       // For more information about these states, see:
       //   https://github.com/okta/okta-signin-widget#rendereloptions-success-error
       }
     });}
